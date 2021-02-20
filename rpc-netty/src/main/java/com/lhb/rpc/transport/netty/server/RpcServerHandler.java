package com.lhb.rpc.transport.netty.server;

import com.lhb.rpc.enums.RpcResponseCode;
import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.handler.RpcRequestHandler;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.request.RpcRequest;
import com.lhb.rpc.transport.command.response.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author BruseLin
 * @Date 2021/2/20 15:06
 * @Version 1.0
 */
@Slf4j
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler;

    public RpcServerHandler() {
        rpcRequestHandler = SingletonFactory.getSingleInstance(RpcRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcMessage) {
                log.info("server receive msg [{}]", msg);
                byte messageType = ((RpcMessage) msg).getMessageType();
                RpcMessage rpcMessage = RpcMessage.builder()
                        .serializerCodec(SerializationType.KRYO.getCode())
                        .messageId(((RpcMessage) msg).getMessageId())
                        .build();
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    rpcMessage.setData(RpcConstants.PONG);
                } else {
                    Object rpcRequest = ((RpcMessage) msg).getData();
                    Object result = rpcRequestHandler.handler((RpcRequest) rpcRequest);
                    log.info("server get result [{}]", result);
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RpcResponse<Object> rpcResponse = RpcResponse.success(((RpcMessage) msg).getMessageId(), result);
                        rpcMessage.setData(rpcResponse);
                    } else {
                        RpcResponse<Object> rpcResponse = RpcResponse.fail(((RpcMessage) msg).getMessageId(), null, RpcResponseCode.FALSE);
                        rpcMessage.setData(rpcResponse);
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
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
                log.info("30 second not receive message, close th connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();

    }
}
