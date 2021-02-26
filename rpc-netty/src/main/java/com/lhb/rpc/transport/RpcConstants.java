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
     * 服务注册类型
     */
    public static final byte REGISTER_TYPE = 3;

    /**
     * 服务发现类型
     */
    public static final byte DISCOVERY_TYPE = 4;

    /**
     * 心跳请求类型
     */
    public static final byte HEARTBEAT_REQUEST_TYPE = 5;

    /**
     * 心跳响应类型
     */
    public static final byte HEARTBEAT_RESPONSE_TYPE = 6;

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
