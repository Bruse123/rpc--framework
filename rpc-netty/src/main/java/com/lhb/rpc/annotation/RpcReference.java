package com.lhb.rpc.annotation;

import java.lang.annotation.*;

/**
 * @author BruseLin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RpcReference {
    /**
     * 服务名称
     */
    String ServiceName();
}
