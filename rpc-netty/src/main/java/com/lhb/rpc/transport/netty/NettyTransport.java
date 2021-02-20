package com.lhb.rpc.transport.netty;

import com.lhb.rpc.transport.Transport;
import com.lhb.rpc.transport.command.request.RpcRequest;
import com.lhb.rpc.transport.command.response.Response;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

/**
 * 使用netty框架发送请求
 *
 * @author BruseLin
 */
public class NettyTransport implements Transport {

    private Channel channel;


    @Override
    public CompletableFuture<Response<Object>> send(RpcRequest request) {

        return null;
    }
}
