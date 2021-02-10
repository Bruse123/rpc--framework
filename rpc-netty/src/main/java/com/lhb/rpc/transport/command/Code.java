package com.lhb.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应请求码
 * @Author BruseLin
 * @Date 2021/2/10 15:41
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum Code {
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

    private int code;
    private String message;
}
