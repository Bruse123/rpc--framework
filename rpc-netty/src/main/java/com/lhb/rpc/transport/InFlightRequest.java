package com.lhb.rpc.transport;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 请求池，控制请求数量
 *
 * @author BruseLin
 */
public class InFlightRequest implements Closeable {

    /**
     * 超时时间
     */
    private final static long TIMEOUT_SEC = 10L;

    /**
     * 10个信号量，及最多只有10个正在进行中的请求
     */
    private final Semaphore semaphore = new Semaphore(10);

    /**
     * 正在进行中的请求
     */
    private final Map<String, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduledExecutorService;

    private final ScheduledFuture scheduledFuture;

    public InFlightRequest() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("regularly_cleanup_expired_requests-pool-%d").build());
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    /**
     * 如果正在进行中的请求超过10秒还未完成，则移除该请求，释放信号量
     */
    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                semaphore.release();
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * 将一个请求加入请求队列中
     */
    public void put(ResponseFuture responseFuture) {
        try {
            if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
                //TIMEOUT_SEC秒内如果能获得到信号量则加入队列中
                futureMap.put(responseFuture.getRequestId(), responseFuture);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据requestId获取请求结果，并释放信号量
     */
    public ResponseFuture remove(String requestId) {
        ResponseFuture responseFuture = futureMap.get(requestId);
        if (responseFuture != null) {
            semaphore.release();
        }
        return responseFuture;
    }

    @Override
    public void close(){
        //关闭当前正在执行的线程
        scheduledFuture.cancel(true);
        //关闭定时任务的线程池
        scheduledExecutorService.shutdown();
    }

}


