package com.lhb.rpc.transport.command;

/**
 * 请求头
 * @author BruseLin
 */
public class Header {

    /**
     * 请求id，唯一标识一个请求
     */
    private String requestId;

    /**
     * 命令类型
     */
    private String type;

    /**
     * 命令的版本号（传输协议的版本号）
     */
    private String version;
}
