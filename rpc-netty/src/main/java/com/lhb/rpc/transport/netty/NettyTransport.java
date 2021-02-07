package com.lhb.rpc.transport.netty;

import com.lhb.rpc.transport.InFlightRequest;
import com.lhb.rpc.transport.ResponseFuture;
import com.lhb.rpc.transport.Transport;
import com.lhb.rpc.transport.command.Command;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

/**
 * 使用netty框架发送请求
 *
 * @author BruseLin
 */
public class NettyTransport implements Transport {

    private Channel channel;

    private InFlightRequest inFlightRequest;

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            //将请求放入队列中
            inFlightRequest.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            channel.writeAndFlush(request).addListener(future -> {
                //发送失败
                if (!future.isSuccess()) {
                    //设置发送失败原因到返回值
                    completableFuture.completeExceptionally(future.cause());
                    //关闭管道
                    channel.close();
                }
            });
        } catch (Throwable t) {
            //发送异常，从队列中移除该请求
            inFlightRequest.remove(request.getHeader().getRequestId());
            //设置发送异常原因到返回值
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}
