package com.lhb.rpc.api.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * spi类加载器辅助类
 *
 * @author BruseLin
 */
public class SpiService {
    /**
     * 单例服务
     */
    private final static Map<String, Object> SINGLETON_SERVICES = new HashMap<>();

    /**
     * 加载路径META-INF/services/全限定类名 下所有配置的类
     */
    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
                .map(SpiService::singletonFilter).collect(Collectors.toList());

    }

    /**
     * 加载路径META-INF/services/全限定类名 下第一个配置的类
     */
    public synchronized static <S> S load(Class<S> service) {
        return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
                .map(SpiService::singletonFilter).findFirst()
                .orElseThrow(() -> new ServiceLoadException(String.format("spi加载配置类失败，Class：%s", service.getCanonicalName())));
    }

    @SuppressWarnings("unchecked")
    private static <S> S singletonFilter(S service) {
        if (service.getClass().isAssignableFrom(Singleton.class)) {
            //如果服务的类带有单例注解，判断集合是否已存在该单例对象
            Object singletonInstance = SINGLETON_SERVICES.putIfAbsent(service.getClass().getCanonicalName(), service);
            return singletonInstance == null ? service : (S) singletonInstance;
        } else {
            return service;
        }

    }

}
