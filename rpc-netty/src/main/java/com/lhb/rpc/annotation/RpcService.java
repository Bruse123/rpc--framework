package com.lhb.rpc.annotation;

import java.lang.annotation.*;

/**
 *
 * @author BruseLin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RpcService {

    /**
     * 服务名称
     */
    String ServiceName();
}
