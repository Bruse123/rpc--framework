package com.lhb.rpc.serializer;

/**
 * 序列化接口
 *
 * @author BruseLin
 */
public interface Serializer {

    /**
     * 将对象序列化为字节数组
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字节数组
     */
    public <T> byte[] serialize(T obj);

    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray 序列化后的字节数组
     * @param <T>       原对象的类型
     * @return 原对象
     */
    public <T> T parse(byte[] byteArray);


}
