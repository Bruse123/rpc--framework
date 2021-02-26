package com.lhb.rpc.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:40
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String requestId;
    private String serviceName;
    private InetSocketAddress inetSocketAddress;
}
