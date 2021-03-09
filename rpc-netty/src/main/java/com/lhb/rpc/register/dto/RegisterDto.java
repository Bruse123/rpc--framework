package com.lhb.rpc.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:40
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterDto {
    /**
     * 请求id
     */
    private String requestId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务地址
     */
    private Address address;
}
