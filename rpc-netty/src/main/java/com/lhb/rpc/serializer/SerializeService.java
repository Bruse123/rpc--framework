package com.lhb.rpc.serializer;

import com.lhb.rpc.spi.SpiLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化使用类
 *
 * @author BruseLin
 */
@SuppressWarnings("unchecked")
public class SerializeService {
    private static final Logger logger = LoggerFactory.getLogger(SerializeService.class);
    /**
     * 用于序列化。key 是序列化实现类对应的序列化对象的类型，
     * 它的用途是在序列化的时候，通过被序列化的对象类型，找到对应的序列化实现类。
     * 可以根据序列化实现类所支持序列化的对象类型获取该序列化实现类的class对象
     * Class<?> 序列化对象类型
     * Serializer<?> 序列化实现类
     */
    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();

    /**
     * 用于反序列化。key 是序列化实现类的类型，用于在反序列化的时候，
     * 从序列化的数据中读出对象类型，然后找到对应的序列化实现类
     * 可以根据序列化实现类自定义的对象类型获取到所序列化的对象类型
     * Byte 序列化实现类自定义的对象类型
     * Class<?> 所序列化的对象类型
     */
    private static Map<Byte, Class<?>> typeMap = new HashMap<>();


    static {
        //初始化时加载所有序列化实现类
        for (Serializer serializer : SpiLoader.loadAllService(Serializer.class)) {
            registry(serializer.serializerCode(), serializer);
            logger.info("registry serializer, class: {}, type: {}.",
                    serializer.getSerializeClass().getCanonicalName(), serializer.serializerCode());
        }
    }

    /**
     * 注册服务
     */
    private static <E> void registry(Byte type, Serializer<E> serializer) {
        typeMap.put(type, serializer.getSerializeClass());
        serializerMap.put(serializer.getSerializeClass(), serializer);
    }


    /**
     * 反序列化对象
     */
    public static <E> E parse(byte[] buffers) {
        return parse(buffers, 0, buffers.length);
    }

    /**
     * 反序列化对象
     *
     * @param buffers 序列化后的字符数组
     * @param offset  数组的偏移量，从这个位置开始写入序列化数据
     * @param length  对象序列化后的长度
     * @param <E>     反序列化后生成对象的类型
     * @return 反序列化后生成的对象
     */
    private static <E> E parse(byte[] buffers, int offset, int length) {
        //自定义的序列化对象类型
        byte type = parseEntryType(buffers);
        //根据自定义的序列化对象类型获取所序列化的对象类型
        @SuppressWarnings("unchecked")
        Class<E> entryClass = (Class<E>) typeMap.get(type);
        if (entryClass == null) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffers, offset + 1, length - 1, entryClass);
        }
    }

    /**
     * 反序列化对象
     *
     * @param buffers    序列化后的字符数组
     * @param offset     数组的偏移量，从这个位置开始写入序列化数据
     * @param length     对象序列化后的长度
     * @param entryClass 所序列化的对象类型
     * @param <E>        反序列化后生成对象的类型
     * @return 反序列化后生成的对象
     */
    private static <E> E parse(byte[] buffers, int offset, int length, Class<E> entryClass) {
        //可以根据序列化实现类所支持序列化的对象类型获取该序列化实现类的class对象
        @SuppressWarnings("unchecked")
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entryClass);
        E parseEntry = serializer.parse(buffers, offset, length);
        if (parseEntry.getClass().isAssignableFrom(entryClass)) {
            return parseEntry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    /**
     * 序列化的对象类型
     */
    private static byte parseEntryType(byte[] buffers) {
        return buffers[0];
    }

    /**
     * 序列化对象
     *
     * @param entry 所需序列化的对象
     * @param <E>   所需序列化的对象类型
     * @return 序列化后的字节数组
     */
    public static <E> byte[] serialize(E entry) {
        @SuppressWarnings("unchecked")
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }
        int length = serializer.size(entry);
        byte[] buffers = new byte[length + 1];
        serializer.serialize(entry, buffers, 1, length);
        return buffers;
    }


}
