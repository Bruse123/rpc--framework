package com.lhb.rpc.center.handler;

import com.lhb.rpc.center.ServerChannelProvider;
import com.lhb.rpc.center.service.ServiceCenter;
import com.lhb.rpc.enums.RpcResponseCode;
import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.dto.Command;
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
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Random;

/**
 * @Author BruseLin
 * @Date 2021/2/23 11:34
 * @Version 1.0
 */
@Slf4j
@Component
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
            if (msg instanceof RpcMessage) {
                log.info("Register Center receive msg：[{}]", msg);
                RpcMessage message = (RpcMessage) msg;
                byte messageType = message.getMessageType();
                @SuppressWarnings("unchecked")
                Command<Object> command = (Command<Object>) message.getData();
                if (messageType == RpcConstants.REGISTER_TYPE) {
                    serviceCenter.register((RegisterDto) command.getData());
                } else if (messageType == RpcConstants.DISCOVERY_TYPE) {
                    DiscoveryDto data = (DiscoveryDto) message.getData();
                    LinkedList<InetSocketAddress> discovery = serviceCenter.discovery(data);
                    RpcResponse<Object> response = null;
                    //随机选取一个服务地址
                    if (discovery.size() > 0) {
                        int size = discovery.size();
                        Random random = new Random();
                        int index = random.nextInt(size);
                        InetSocketAddress inetSocketAddress = discovery.get(index);
                        response = RpcResponse.success(command.getRequestId(), inetSocketAddress);
                    } else {
                        response = RpcResponse.fail(command.getRequestId(), null, RpcResponseCode.NO_PROVIDER);
                    }
                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                }
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
                log.info("read server timeout, send Heartbeat [{}]", ctx.channel().localAddress());
                RpcMessage rpcMessage = RpcMessage.builder().transportVersion(RpcConstants.TRANSPORT_VERSION)
                        .serializerCodec(SerializationType.KRYO.getCode())
                        .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                        .data(RpcConstants.PING)
                        .build();
                SocketAddress remoteAddress = ctx.channel().remoteAddress();
                Channel channel = serverChannelProvider.get((InetSocketAddress) remoteAddress);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
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
