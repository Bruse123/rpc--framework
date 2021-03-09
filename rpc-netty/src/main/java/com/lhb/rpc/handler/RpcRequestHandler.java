package com.lhb.rpc.handler;

import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.provider.ServiceProvider;
import com.lhb.rpc.provider.ServiceProviderImpl;
import com.lhb.rpc.transport.command.request.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author BruseLin
 * @Date 2021/2/20 15:19
 * @Version 1.0
 */
@Slf4j
public class RpcRequestHandler {

    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getSingleInstance(ServiceProviderImpl.class);
    }

    public Object handler(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.toRpcServiceProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getArguments());
            log.info("service [{}] invoke method [{}]", rpcRequest.getServiceName(), method.getName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            //throw new RpcException(e);
            log.error(e.getMessage());
        }
        return result;
    }

}
