package com.lhb.rpc.transport.netty.codec;

import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.serializer.Serializer;
import com.lhb.rpc.spi.SpiLoader;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * |                                  消息头                    |     消息体
 * |消息类型 1B   传输协议 1B  消息序列号 4B  消息长度 4B 序列化类型 1B | body（object序列化）
 * 这里消息长度=消息头+消息体
 * @Author BruseLin
 * @Date 2021/2/18 15:46
 * @Version 1.0
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final AtomicInteger MSG_ID = new AtomicInteger(0);


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) {
        try {
            //写入数据
            //消息类型
            byte messageType = msg.getMessageType();
            out.writeByte(messageType);
            //传输协议
            out.writeByte(RpcConstants.TRANSPORT_VERSION);
            //消息序列号
            out.writeInt(MSG_ID.getAndIncrement());
            //消息长度（空4字节，后面写长度）
            out.writerIndex(out.writerIndex() + 4);
            //序列化类型
            out.writeByte(SerializationType.KRYO.getCode());
            //序列化消息体
            byte[] bodyBytes = null;
            Object requestData = msg.getData();
            int fullLength = RpcConstants.HEAD_LENGTH;
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                log.info("Encode object {}", requestData);
                Class<?> serializerClass = SerializationType.getClazz(msg.getSerializerCodec());
                Serializer serializer = SpiLoader.getSpiLoader(Serializer.class).getService(serializerClass);
                bodyBytes = serializer.serialize(requestData);
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writerIndex = out.writerIndex();
            //调整buffer的写下标到消息长度的起始下标
            out.writerIndex(writerIndex - fullLength + RpcConstants.HEAD_LENGTH - RpcConstants.LENGTH_FIELD_LENGTH - 1);
            out.writeInt(fullLength);
            out.writerIndex(writerIndex);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
