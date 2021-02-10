package com.lhb.rpc.serializer.impl;

import com.lhb.rpc.transport.command.request.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化实现类支持的对象类型（自定义）
 *
 * @author BruseLin
 */
@AllArgsConstructor
@Getter
public enum SerializerType {
    /**
     * {@link String}对象类型
     */
    STRING_TYPE((byte) 0x01, "String"),
    /**
     * {@link RpcRequest} 对象类型
     */
    RPC_REQUEST_TYPE((byte) 0x03, "RpcRequest");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializerType serializeType : SerializerType.values()) {
            if (serializeType.getCode() == code) {
                return serializeType.name;
            }
        }
        return null;
    }


}
