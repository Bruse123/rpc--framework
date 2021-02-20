package com.lhb.rpc.transport.netty.codec;

import com.lhb.rpc.enums.SerializationType;
import com.lhb.rpc.serializer.Serializer;
import com.lhb.rpc.spi.SpiLoader;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * |                                  消息头                    |     消息体
 * |消息类型 1B   传输协议 1B  消息序列号 4B  消息长度 4B 序列化类型 1B | body（object序列化）
 * 这里消息长度=消息头+消息体
 *
 * @Author BruseLin
 * @Date 2021/2/20 10:45
 * @Version 1.0
 * @see <a href="https://www.cnblogs.com/java-chen-hao/p/11571229.html">LengthFieldBasedFrameDecoder解码器</a>
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {


    /**
     * @param maxFrameLength      最大帧长度。也就是可以接收的数据的最大长度。如果超过，此次数据会被丢弃。
     * @param lengthFieldOffset   长度域偏移。就是说数据开始的几个字节可能不是表示数据长度，需要后移几个字节才是长度域。
     * @param lengthFieldLength   长度域字节数。用几个字节来表示数据长度。
     * @param lengthAdjustment    数据长度修正。因为长度域指定的长度可以使header+body的整个长度，也可以只是body的长度。如果表示header+body的整个长度，那么我们需要修正数据长度。
     * @param initialBytesToStrip 跳过的字节数。如果你需要接收header+body的所有数据，此值就是0，如果你只想接收body数据，那么需要跳过header所占用的字节数。
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public RpcMessageDecoder() {
        super(RpcConstants.MAX_FRAME_LENGTH, 6, 4, -10, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //调用父类方法判断拆包、粘包、帧过大逻辑，返回有效帧的切片（即消息长度字段所指的数据内容）
        Object decode = super.decode(ctx, in);
        if (decode instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) decode;
            if (buf.readableBytes() >= RpcConstants.HEAD_LENGTH) {
                try {
                    return decodeFrame(buf);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    buf.release();
                }
            }
        }
        return decode;
    }

    private Object decodeFrame(ByteBuf in) {
        //校验消息类型是否符合
        byte messageType = in.readByte();
        //校验传输协议是否支持
        byte transportVersion = in.readByte();
        int messageId = in.readInt();
        int fullLength = in.readInt();
        byte codec = in.readByte();
        RpcMessage rpcMessage = RpcMessage.builder().messageType(messageType)
                .transportVersion(transportVersion)
                .messageId(messageId)
                .serializerCodec(codec)
                .build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] dataBytes = new byte[bodyLength];
            in.readBytes(dataBytes);
            Class<?> clazz = SerializationType.getClazz(codec);
            Serializer serializer = SpiLoader.getSpiLoader(Serializer.class).getService(clazz);
            Object data = serializer.parse(dataBytes);
            rpcMessage.setData(data);
        }
        return rpcMessage;
    }
}
