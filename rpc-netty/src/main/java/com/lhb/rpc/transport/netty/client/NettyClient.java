package com.lhb.rpc.transport.netty.client;

import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.ServiceDiscovery;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.Transport;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.request.RpcRequest;
import com.lhb.rpc.transport.command.response.RpcResponse;
import com.lhb.rpc.transport.netty.codec.RpcMessageDecoder;
import com.lhb.rpc.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author BruseLin
 * @Date 2021/2/22 9:59
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyClient implements Transport {

    private final ServiceDiscovery serviceDiscovery;

    private final ChannelProvider channelProvider;

    private final UnProcessedRequests unProcessedRequests;

    private final NioEventLoopGroup nioEventLoopGroup;

    private final Bootstrap bootstrap;

    public NettyClient() {
        this.nioEventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS))
                                .addLast(new RpcMessageEncoder())
                                .addLast(new RpcMessageDecoder())
                                .addLast(new RpcClientHandler());
                    }
                });
        this.unProcessedRequests = SingletonFactory.getSingleInstance(UnProcessedRequests.class);
        this.channelProvider = SingletonFactory.getSingleInstance(ChannelProvider.class);
        this.serviceDiscovery = SingletonFactory.getSingleInstance(ServiceDiscovery.class);
    }

    @Override
    public CompletableFuture<RpcResponse<Object>> send(RpcRequest request) {
        CompletableFuture<RpcResponse<Object>> responseFuture = new CompletableFuture<>();
        String serviceName = request.getServiceName();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) serviceDiscovery.lookupService(serviceName);
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            unProcessedRequests.putRequest(request.getRequestId(), responseFuture);
            RpcMessage rpcMessage = RpcMessage.builder()
                    .messageType(RpcConstants.REQUEST_TYPE)
                    .serializerCodec(SerializationType.KRYO.getCode())
                    .transportVersion(RpcConstants.TRANSPORT_VERSION)
                    .data(request)
                    .build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("send msg success [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    responseFuture.completeExceptionally(future.cause());
                    log.error("send msg fail", future.cause());
                }
            });
        }
        return responseFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.setChannel(inetSocketAddress, channel);
        }
        return channel;
    }

    @SneakyThrows
    private Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("connect to [{}]", inetSocketAddress);
                completableFuture.complete(future.channel());
            }
        });
        return completableFuture.get();
    }

    public void close() {
        this.nioEventLoopGroup.shutdownGracefully();
    }
}
