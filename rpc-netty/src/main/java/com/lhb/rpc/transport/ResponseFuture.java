package com.lhb.rpc.transport;

import com.lhb.rpc.transport.command.Command;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * 异步获取请求结果
 * @author BruseLin
 */
@Data
public class ResponseFuture {
    /**
     * 请求id，唯一标识一个请求
     */
    private final String requestId;
    /**
     * 异步获取请求结果
     */
    private final CompletableFuture<Command> future;
    /**
     * 请求时间，用于判断请求是否超时
     */
    private final long timestamp;

    public ResponseFuture(String requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }
}
