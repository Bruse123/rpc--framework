package com.lhb.rpc.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取序列化实现类
 * @author BruseLin
 */
public class SerializeSupport {

    /**
     * 用于序列化。key 是序列化实现类对应的序列化对象的类型，
     * 它的用途是在序列化的时候，通过被序列化的对象类型，找到对应的序列化实现类。
     * Class<?> 序列化对象类型
     * Serializer<?> 序列化实现类
     */
    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();

    /**
     * 用于反序列化。key 是序列化实现类的类型，用于在反序列化的时候，
     * 从序列化的数据中读出对象类型，然后找到对应的序列化实现类
     * Byte 序列化实现类的类型
     * Class<?> 序列化对象类型
     */
    private static Map<Byte, Class<?>> classMap = new HashMap<>();



}
