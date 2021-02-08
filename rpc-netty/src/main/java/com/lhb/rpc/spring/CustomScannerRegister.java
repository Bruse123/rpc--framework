package com.lhb.rpc.spring;

import com.lhb.rpc.annotation.RpcScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

/**
 * @author BruseLin
 */
@Slf4j
public class CustomScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private final static String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
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
            //如果没有设置basePackage 扫描路径,就扫描对应包下面的值
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
        CustomScan rpcServiceScan = new CustomScan(registry, RpcScan.class);
        if (resourceLoader != null) {
            rpcServiceScan.setResourceLoader(resourceLoader);
        }
        int scanCount = rpcServiceScan.scan(rpcScanBasePackages);
        log.info("rpcServiceScanner扫描的数量 [{}]", scanCount);
    }
}
