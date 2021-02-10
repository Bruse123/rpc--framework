package com.lhb.rpc.transport.command.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求、响应数据
 *
 * @author BruseLin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcRequest {

    private String interfaceName;
    private String methodName;
    private byte[] serializedArguments;



}
