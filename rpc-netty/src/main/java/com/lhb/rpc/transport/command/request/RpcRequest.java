package com.lhb.rpc.transport.command.request;

import com.lhb.rpc.service.RpcServiceProperties;
import com.lhb.rpc.transport.command.Header;
import lombok.*;

/**
 * 请求、响应数据
 *
 * @author BruseLin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Getter
public class RpcRequest extends Header {

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] arguments;
    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    @Builder
    RpcRequest(String requestId, String serviceName, String methodName,
               Object[] arguments, Class<?>[] paramTypes) {
        super(requestId);
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.arguments = arguments;
        this.paramTypes = paramTypes;
    }

    public RpcServiceProperties toRpcServiceProperties(){
        return RpcServiceProperties.builder().serviceName(this.serviceName).build();
    }
}
