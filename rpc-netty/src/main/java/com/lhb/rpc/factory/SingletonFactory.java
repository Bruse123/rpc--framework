package com.lhb.rpc.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取单例对象的工厂类
 *
 * @author BruseLin
 */
public class SingletonFactory {

    /**
     * 单例集合
     */
    private final static Map<String, Object> SINGLE_MAP = new HashMap<>();

    private SingletonFactory() {

    }

    public static <T> T getSingleInstance(Class<T> tClass) {
        String key = tClass.toString();
        Object instance;
        synchronized (SingletonFactory.class) {
            instance = SINGLE_MAP.get(key);
            if (instance == null) {
                try {
                    instance = tClass.getDeclaredConstructor().newInstance();
                    SINGLE_MAP.put(key, instance);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return tClass.cast(instance);
    }
}
