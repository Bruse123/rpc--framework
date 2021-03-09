package com.lhb.rpc.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author BruseLin
 * @Date 2021/3/5 15:49
 * @Version 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    /**
     * 服务IP
     */
    private String ip;

    /**
     * 服务端口
     */
    private int port;
}
