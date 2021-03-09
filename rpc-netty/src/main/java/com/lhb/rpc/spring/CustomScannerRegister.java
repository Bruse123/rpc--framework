package com.lhb.rpc.spring;

import com.lhb.rpc.annotation.RpcScan;
import com.lhb.rpc.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @author BruseLin
 */
@Slf4j
public class CustomScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private final static String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private final static String SPRING_BEAN_BASE_PACKAGE = "com.lhb.rpc.spring";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //获取注解所有的属性和值
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        //获取basePackage属性的值
        String[] rpcScanBasePackages = new String[0];
        if (annotationAttributes != null) {
            rpcScanBasePackages = annotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (rpcScanBasePackages.length == 0) {
            //如果没有设置basePackage 扫描路径,就扫描带注解的类的包
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
        CustomScan rpcServiceScan = new CustomScan(registry, RpcService.class);
        CustomScan springBeanScan = new CustomScan(registry, Component.class);
        if (resourceLoader != null) {
            rpcServiceScan.setResourceLoader(resourceLoader);
            springBeanScan.setResourceLoader(resourceLoader);
        }
        int springBeanCount = springBeanScan.scan(SPRING_BEAN_BASE_PACKAGE);
        log.info("springBeanScan扫描的数量 [{}]", springBeanCount);
        int rpcServiceCount = rpcServiceScan.scan(rpcScanBasePackages);
        log.info("rpcServiceScan扫描的数量 [{}]", rpcServiceCount);
    }
}
