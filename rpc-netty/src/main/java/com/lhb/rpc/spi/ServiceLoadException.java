package com.lhb.rpc.spi;


/**
 * @Author BruseLin
 * @Date 2021/2/9 14:43
 * @Version 1.0
 */
public class ServiceLoadException extends RuntimeException {
    public ServiceLoadException(String message) {
        super(message);
    }
}
