package com.lhb.rpc.serializer;

/**
 * @author BruseLin
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}
