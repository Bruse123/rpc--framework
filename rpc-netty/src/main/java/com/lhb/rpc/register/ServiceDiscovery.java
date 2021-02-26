package com.lhb.rpc.register;

import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.client.ServiceClient;
import com.lhb.rpc.register.dto.Command;
import com.lhb.rpc.register.dto.DiscoveryDto;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.response.RpcResponse;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:13
 * @Version 1.0
 */
public class ServiceDiscovery {
    private final ServiceClient serviceRegisterClient;

    public ServiceDiscovery() {
        serviceRegisterClient = SingletonFactory.getSingleInstance(ServiceClient.class);
    }

    @SneakyThrows
    public Object lookupService(String serviceName) {
        DiscoveryDto discoveryDto = new DiscoveryDto(UUID.randomUUID().toString(), serviceName);
        Command<Object> command = new Command<>();
        command.setData(discoveryDto);
        RpcMessage message = RpcMessage.builder()
                .messageType(RpcConstants.REGISTER_TYPE)
                .data(command)
                .build();
        CompletableFuture<RpcResponse<Object>> future = serviceRegisterClient.send(message);
        RpcResponse<Object> rpcResponse = future.get();
        return rpcResponse.getData();
    }
}
