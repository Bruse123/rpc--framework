package com.lhb.rpc.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:40
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscoveryDto {
    private String requestId;
    private String serviceName;
}
