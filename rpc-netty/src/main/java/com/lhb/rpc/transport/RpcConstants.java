package com.lhb.rpc.transport;

/**
 * @Author BruseLin
 * @Date 2021/2/19 16:55
 * @Version 1.0
 */
public class RpcConstants {

    /**
     * 传输协议
     */
    public static final byte TRANSPORT_VERSION = 1;

    /**
     * 消息头长度字段的长度（单位：字节）
     */
    public static final int LENGTH_FIELD_LENGTH = 4;

    /**
     * 消息帧头部长度
     */
    public static final int HEAD_LENGTH = 11;

    /**
     * 请求类型
     */
    public static final byte REQUEST_TYPE = 1;

    /**
     * 响应类型
     */
    public static final byte RESPONSE_TYPE = 2;

    /**
     * 服务注册请求类型
     */
    public static final byte REGISTER_REQUEST_TYPE = 11;

    /**
     * 服务注册响应类型
     */
    public static final byte REGISTER_RESPONSE_TYPE = 12;

    /**
     * 服务发现请求类型
     */
    public static final byte DISCOVERY_REQUEST_TYPE = 13;

    /**
     * 服务发现响应类型
     */
    public static final byte DISCOVERY_RESPONSE_TYPE = 14;

    /**
     * 心跳请求类型
     */
    public static final byte HEARTBEAT_REQUEST_TYPE = 21;

    /**
     * 心跳响应类型
     */
    public static final byte HEARTBEAT_RESPONSE_TYPE = 22;

    /**
     * 心跳请求内容
     */
    public static final String PING = "ping";

    /**
     * 心跳响应内容
     */
    public static final String PONG = "pong";

    /**
     * 最大帧长度
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
