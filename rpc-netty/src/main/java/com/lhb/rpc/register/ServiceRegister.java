package com.lhb.rpc.register;

import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.register.client.ServiceClient;
import com.lhb.rpc.register.dto.Command;
import com.lhb.rpc.register.dto.RegisterDto;
import com.lhb.rpc.transport.RpcConstants;
import com.lhb.rpc.transport.command.RpcMessage;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @Author BruseLin
 * @Date 2021/2/22 15:13
 * @Version 1.0
 */
public class ServiceRegister {

    private final ServiceClient serviceRegisterClient;

    public ServiceRegister() {
        serviceRegisterClient = SingletonFactory.getSingleInstance(ServiceClient.class);
    }

    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        RegisterDto registerDto = new RegisterDto(UUID.randomUUID().toString(), serviceName, inetSocketAddress);
        Command<Object> command = new Command<>();
        command.setData(registerDto);
        RpcMessage message = RpcMessage.builder()
                .messageType(RpcConstants.REGISTER_TYPE)
                .data(command)
                .build();
        serviceRegisterClient.send(message);
    }
}
