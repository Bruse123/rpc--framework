package com.lhb.rpc.register.handler;

import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.client.ServiceClient;
import com.lhb.rpc.register.dto.Command;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.response.RpcResponse;
import com.lhb.rpc.transport.netty.client.UnProcessedRequests;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:31
 * @Version 1.0
 */
@Slf4j
public class RegisterHandler extends ChannelInboundHandlerAdapter {

    private final UnProcessedRequests unProcessedRequests;

    private final ServiceClient serviceClient;

    public RegisterHandler() {
        unProcessedRequests = SingletonFactory.getSingleInstance(UnProcessedRequests.class);
        serviceClient = SingletonFactory.getSingleInstance(ServiceClient.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("registerClient receive msg [{}]", msg);
            if(msg instanceof Command){
                @SuppressWarnings("unchecked")
                Command<Object> command = (Command<Object>) msg;

            }else if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                byte messageType = rpcMessage.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("heart [{}]", rpcMessage.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    @SuppressWarnings("unchecked")
                    RpcResponse<Object> response = (RpcResponse<Object>) rpcMessage.getData();
                    unProcessedRequests.complete(response);
                }
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("register write trigger [{}]", ctx.channel().localAddress());

                RpcMessage rpcMessage = RpcMessage.builder().transportVersion(RpcConstants.TRANSPORT_VERSION)
                        .serializerCodec(SerializationType.KRYO.getCode())
                        .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                        .data(RpcConstants.PING)
                        .build();
                Channel channel = serviceClient.getChannel();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Register client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
