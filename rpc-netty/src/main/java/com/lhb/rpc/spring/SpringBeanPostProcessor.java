package com.lhb.rpc.spring;

import com.lhb.rpc.annotation.RpcReference;
import com.lhb.rpc.annotation.RpcService;
import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.provider.ServiceProvider;
import com.lhb.rpc.provider.ServiceProviderImpl;
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


    public SpringBeanPostProcessor() {
        this.SERVICE_PROVIDER = SingletonFactory.getSingleInstance(ServiceProviderImpl.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("found RpcService  [{}]", bean.getClass().getName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            String serviceName = rpcService.ServiceName();
            SERVICE_PROVIDER.publishService(bean, serviceName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if(rpcReference != null){
                String serviceName = rpcReference.ServiceName();

            }
        }

        return bean;
    }
}
