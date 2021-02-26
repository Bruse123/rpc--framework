package com.lhb.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author BruseLin
 * @Date 2021/2/18 14:32
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMsg {
    /**
     * 异常返回值
     */
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配");;

    private final String message;
}
















