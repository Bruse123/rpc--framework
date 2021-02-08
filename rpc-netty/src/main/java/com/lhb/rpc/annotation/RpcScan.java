package com.lhb.rpc.annotation;

import com.lhb.rpc.spring.CustomScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author BruseLin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegister.class)
@Documented
public @interface RpcScan {

    /**
     * 要扫描的包路径
     */
    String[] basePackage();
}
