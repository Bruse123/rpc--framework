package com.lhb.rpc.serializer;

/**
 * 序列化接口
 *
 * @author BruseLin
 */
public interface Serializer<T> {

    /**
     * 序列化实现类支持的对象类型
     * 用一个字节标识对象类型，每种类型的数据应该具有不同的类型值
     * @return byte
     */
    byte type();

    /**
     * 计算序列化后的数据长度，用于申请存放序列化后数据的字节数组
     * @param entry 序列化对象
     * @return 对象序列化后的长度
     */
    int size(T entry);


    /**
     * 序列化对象
     *
     * @param entry  待序列化的对象
     * @param buffers  存放序列化后的数据的字节数组
     * @param offset 存放序列化后数据的起始位置
     * @param length 序列化后数据的长度
     */
    void serialize(T entry, byte[] buffers, int offset, int length);

    /**
     * 反序列化对象
     *
     * @param buffers  存放序列化数据的字节数组
     * @param offset 存放序列化后数据的起始位置
     * @param length 序列化后数据的长度
     * @return 反序列化生成的对象
     */
    T parse(byte[] buffers, int offset, int length);

    /**
     * 获取这个序列化实现类所支持序列化的class对象
     * @return 序列化实现类支持序列化的Class对象
     */
    Class<T> getSerializeClass();


}