package com.lhb.rpc.transport.command;

import lombok.Data;

/**
 * 请求、响应数据
 * @author BruseLin
 */
@Data
public class Command {

    /**
     * 请求头
     */
    protected Header header;

    /**
     * 携带数据
     */
    private byte[] data;

}
