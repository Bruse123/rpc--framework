package com.lhb.rpc.center.service;

import com.lhb.rpc.register.dto.DiscoveryDto;
import com.lhb.rpc.register.dto.RegisterDto;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author BruseLin
 * @Date 2021/2/22 17:40
 * @Version 1.0
 */
public class ServiceCenter {

    /**
     * 可用的服务
     */
    private final Map<String, LinkedList<InetSocketAddress>> servicesMap;

    /**
     * 不可用的服务（无法连接服务端导致服务不可用）
     */
    private final Map<String, LinkedList<InetSocketAddress>> unavailableServicesMap;

    public ServiceCenter() {
        servicesMap = new ConcurrentHashMap<>();
        unavailableServicesMap = new ConcurrentHashMap<>();
    }

    /**
     * 寻找服务
     */
    public LinkedList<InetSocketAddress> discovery(DiscoveryDto discoveryDto) {
        return servicesMap.get(discoveryDto.getServiceName());
    }

    /**
     * 注册服务
     */
    public void register(RegisterDto registerDto) {
        LinkedList<InetSocketAddress> addresses = servicesMap.get(registerDto.getServiceName());
        if (addresses == null) {
            addresses = new LinkedList<>();
        }
        addresses.add(registerDto.getInetSocketAddress());
        servicesMap.put(registerDto.getServiceName(), addresses);
    }

    /**
     * 将不可用的服务移除
     */
    public void unavailable(String serviceName, InetSocketAddress inetSocketAddress) {
        LinkedList<InetSocketAddress> inetSocketAddresses = servicesMap.get(serviceName);
        inetSocketAddresses.remove(inetSocketAddress);
        LinkedList<InetSocketAddress> unavailableAddresses = unavailableServicesMap.getOrDefault(serviceName, new LinkedList<>());
        unavailableAddresses.add(inetSocketAddress);
    }

    /**
     * 服务变可用，移出不可用队列
     */
    public void available(String serviceName, InetSocketAddress inetSocketAddress) {
        LinkedList<InetSocketAddress> unavailableAddresses = unavailableServicesMap.get(serviceName);
        if (unavailableAddresses != null) {
            unavailableAddresses.remove(inetSocketAddress);
        }
        LinkedList<InetSocketAddress> inetSocketAddresses = servicesMap.get(serviceName);
        inetSocketAddresses.remove(inetSocketAddress);
    }
}
