package com.lhb.rpc.spring;

import com.lhb.rpc.annotation.RpcReference;
import com.lhb.rpc.annotation.RpcService;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.provider.ServiceProvider;
import com.lhb.rpc.provider.ServiceProviderImpl;
import com.lhb.rpc.proxy.RpcClientProxy;
import com.lhb.rpc.service.RpcServiceProperties;
import com.lhb.rpc.spi.SpiLoader;
import com.lhb.rpc.transport.Transport;
import com.lhb.rpc.transport.netty.NettyTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author BruseLin
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider SERVICE_PROVIDER;
    private final NettyTransport nettyTransport;

    public SpringBeanPostProcessor() {
        this.SERVICE_PROVIDER = SingletonFactory.getSingleInstance(ServiceProviderImpl.class);
        this.nettyTransport = (NettyTransport) SpiLoader.getSpiLoader(Transport.class).getService(NettyTransport.class);
    }

    //todo
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("found RpcService  [{}]", bean.getClass().getName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().serviceName(rpcService.ServiceName()).build();
            SERVICE_PROVIDER.publishService(bean, rpcServiceProperties);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                //为该bean对象中添加@RpcReference注解的属性生成代理对象
                String serviceName = rpcReference.ServiceName();
                RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().serviceName(serviceName).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyTransport, rpcServiceProperties);
                Object proxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    //将生成的代理对象设置到对象属性中
                    declaredField.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
