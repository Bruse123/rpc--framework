package com.lhb.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author BruseLin
 * @Date 2021/2/10 16:11
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
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
