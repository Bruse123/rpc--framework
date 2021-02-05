package com.lhb.rpc.api.spi;

/**
 * @author BruseLin
 */
public class ServiceLoadException extends RuntimeException {
    public ServiceLoadException(String msg) {
        super(msg);
    }

    public ServiceLoadException(Throwable throwable) {
        super(throwable);
    }
}
