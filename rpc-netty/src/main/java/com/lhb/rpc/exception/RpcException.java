package com.lhb.rpc.exception;

import com.lhb.rpc.enums.RpcErrorMsg;

/**
 * @Author BruseLin
 * @Date 2021/2/18 14:24
 * @Version 1.0
 */
public class RpcException extends RuntimeException {
    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(Throwable throwable) {
        super(throwable);
    }

    public RpcException(RpcErrorMsg rpcErrorMsg) {
        super(rpcErrorMsg.getMessage());
    }
    public RpcException(RpcErrorMsg rpcErrorMsg, String detail) {
        super(rpcErrorMsg.getMessage() + ":" + detail);
    }
}
