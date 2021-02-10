package com.lhb.rpc.service;

import lombok.*;

/**
 * @Author BruseLin
 * @Date 2021/2/10 15:18
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties {

    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName();
    }
}
