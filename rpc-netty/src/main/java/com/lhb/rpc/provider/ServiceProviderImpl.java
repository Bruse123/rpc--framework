package com.lhb.rpc.provider;

import com.lhb.rpc.server.NettyServer;
import com.lhb.rpc.service.RpcServiceProperties;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author BruseLin
 * @Date 2021/2/9 10:23
 * @Version 1.0
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private final Map<String, Object> serviceMap;

    private final Map<Object, InetSocketAddress> addressMap;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        addressMap = new ConcurrentHashMap<>();
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            String serviceName = rpcServiceProperties.getServiceName();
            if (!serviceMap.containsKey(serviceName)) {
                serviceMap.put(serviceName, service);
                log.info("Add service: {}", serviceName);
            }
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, NettyServer.PORT);
            if (addressMap.containsKey(service)) {

            }
        } catch (UnknownHostException e) {
            log.error("found error in getLocalHost", e);
        }
    }

    @Override
    public void getService(RpcServiceProperties rpcServiceProperties) {

    }
}
