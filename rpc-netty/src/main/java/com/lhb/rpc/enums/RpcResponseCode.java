package com.lhb.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author BruseLin
 * @Date 2021/2/19 15:52
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {
    /**
     * 请求成功
     */
    SUCCESS(1, "SUCCESS"),
    /**
     * 请求失败
     */
    FALSE(0, "FALSE"),
    /**
     * 没有该服务
     */
    NO_PROVIDER(-1, "NO_PROVIDER"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-2, "UNKNOWN_ERROR");
    private final int code;

    private final String message;
}
