package com.lhb.rpc.register;

import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.client.ServiceClient;
import com.lhb.rpc.register.dto.Address;
import com.lhb.rpc.register.dto.RegisterDto;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;
import com.lhb.rpc.transport.command.response.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:13
 * @Version 1.0
 */
@Slf4j
public class ServiceRegister {

    private final ServiceClient serviceRegisterClient;

    public ServiceRegister() {
        serviceRegisterClient = SingletonFactory.getSingleInstance(ServiceClient.class);
    }

    @SneakyThrows
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        String ip = inetSocketAddress.getHostString();
        int port = inetSocketAddress.getPort();
        Address address = new Address(ip, port);
        RegisterDto registerDto = new RegisterDto(UUID.randomUUID().toString(), serviceName, address);
        RpcMessage message = RpcMessage.builder()
                .messageType(RpcConstants.REGISTER_REQUEST_TYPE)
                .data(registerDto)
                .build();
        CompletableFuture<RpcResponse<Object>> future = serviceRegisterClient.send(message);
        RpcResponse<Object> response = future.get();
        log.info("register service [{}] to [{}] success", serviceName, inetSocketAddress.getAddress());
    }
}
