package com.lhb.rpc.enums;

import com.lhb.rpc.serializer.impl.KryoSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author BruseLin
 * @Date 2021/2/19 17:44
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum SerializationType {

    /**
     * KRYO
     */
    KRYO((byte) 0x01, KryoSerializer.class);

    private final byte code;
    private final Class<?> Clazz;

    public static Class<?> getClazz(byte code) {
        for (SerializationType c : SerializationType.values()) {
            if (c.getCode() == code) {
                return c.Clazz;
            }
        }
        return null;
    }
}
