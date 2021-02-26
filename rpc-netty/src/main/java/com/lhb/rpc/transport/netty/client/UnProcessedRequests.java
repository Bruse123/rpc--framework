package com.lhb.rpc.transport.netty.client;

import com.lhb.rpc.transport.command.response.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author BruseLin
 * @Date 2021/2/22 10:14
 * @Version 1.0
 */
public class UnProcessedRequests {
    private final static Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_REQUEST = new ConcurrentHashMap<>();

    public void putRequest(String requestId, CompletableFuture<RpcResponse<Object>> completableFuture) {
        UNPROCESSED_REQUEST.put(requestId, completableFuture);
    }

    public void complete(RpcResponse<Object> rpcResponse) {
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_REQUEST.remove(rpcResponse.getRequestId());
        if (future != null) {
            future.complete(rpcResponse);
        }
    }
}
