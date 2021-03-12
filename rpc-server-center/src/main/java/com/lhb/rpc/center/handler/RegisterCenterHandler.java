package com.lhb.rpc.center.handler;

import com.lhb.rpc.center.ServerChannelProvider;
import com.lhb.rpc.center.service.ServiceCenter;
import com.lhb.rpc.enums.RpcErrorMsg;
import com.lhb.rpc.enums.RpcResponseCode;
import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.dto.Address;
import com.lhb.rpc.register.dto.DiscoveryDto;
import com.lhb.rpc.register.dto.RegisterDto;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.response.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

/**
 * @Author BruseLin
 * @Date 2021/2/23 11:34
 * @Version 1.0
 */
@Slf4j
public class RegisterCenterHandler extends ChannelInboundHandlerAdapter {

    private final ServiceCenter serviceCenter;

    private final ServerChannelProvider serverChannelProvider;

    public RegisterCenterHandler() {
        serviceCenter = SingletonFactory.getSingleInstance(ServiceCenter.class);
        serverChannelProvider = SingletonFactory.getSingleInstance(ServerChannelProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //只处理自定义的消息
            if (msg instanceof RpcMessage) {
                log.info("Register Center receive msg：[{}]", msg);
                RpcMessage message = (RpcMessage) msg;
                byte messageType = message.getMessageType();
                RpcMessage responseMessage = RpcMessage.builder().transportVersion(RpcConstants.TRANSPORT_VERSION)
                        .serializerCodec(SerializationType.KRYO.getCode())
                        .messageId(message.getMessageId())
                        .build();
                RpcResponse<Object> response = null;
                if (messageType == RpcConstants.REGISTER_REQUEST_TYPE) {
                    //服务注册的消息
                    RegisterDto data = (RegisterDto) message.getData();
                    serviceCenter.register(data, ctx.channel());
                    responseMessage.setMessageType(RpcConstants.REGISTER_RESPONSE_TYPE);
                    response = RpcResponse.success(data.getRequestId(), String.format("注册服务：%s 成功", data.getServiceName()));
                    //保存服务提供者的通道
                    serverChannelProvider.put(ctx.channel(), (InetSocketAddress) ctx.channel().remoteAddress());
                } else if (messageType == RpcConstants.DISCOVERY_REQUEST_TYPE) {
                    //客户端寻找服务的消息
                    responseMessage.setMessageType(RpcConstants.DISCOVERY_RESPONSE_TYPE);
                    DiscoveryDto data = (DiscoveryDto) message.getData();
                    LinkedList<Address> discovery = serviceCenter.discovery(data);
                    //随机选取一个服务地址
                    if (discovery != null && discovery.size() > 0) {
                        int size = discovery.size();
                        Random random = new Random();
                        int index = random.nextInt(size);
                        Address address = discovery.get(index);
                        response = RpcResponse.success(data.getRequestId(), address);
                    } else {
                        response = RpcResponse.fail(data.getRequestId(), RpcErrorMsg.SERVICE_CAN_NOT_BE_FOUND, RpcResponseCode.NO_PROVIDER);
                    }
                }
                responseMessage.setData(response);
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    log.info("heart [{}]", message.getData());
                    responseMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    responseMessage.setData(RpcConstants.PONG);

                } else if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("heart [{}]", message.getData());
                    return;
                }
                ctx.writeAndFlush(responseMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {

                SocketAddress remoteAddress = ctx.channel().remoteAddress();
                //只保持与服务提供者之间的连接
                Channel channel = serverChannelProvider.get((InetSocketAddress) remoteAddress);
                if (channel != null && channel.isActive()) {
                    log.info("[{}] read server timeout, send Heartbeat [{}]", new Date(), ctx.channel().remoteAddress());
                    RpcMessage rpcMessage = RpcMessage.builder().transportVersion(RpcConstants.TRANSPORT_VERSION)
                            .serializerCodec(SerializationType.KRYO.getCode())
                            .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                            .data(RpcConstants.PING)
                            .build();
                    //ctx.writeAndFlush是从当前ChannelHandler开始，逆序向前执行OutboundHandler。
                    //ctx.channel().writeAndFlush 是从最后一个OutboundHandler开始，依次逆序向前执行其他OutboundHandler，
                    //即使最后一个OutboundHandler在InboundHandler之前，也会执行该OutboundHandler。
                    channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess()) {
                            serviceCenter.unavailable(future.channel());
                            future.channel().close();
                        }
                    });
                } else {
                    serverChannelProvider.remove((InetSocketAddress) remoteAddress);
                    serviceCenter.unavailable(channel);
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Server Center catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
