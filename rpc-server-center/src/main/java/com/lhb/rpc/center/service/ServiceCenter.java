package com.lhb.rpc.center.service;

import com.lhb.rpc.register.dto.Address;
import com.lhb.rpc.register.dto.DiscoveryDto;
import com.lhb.rpc.register.dto.RegisterDto;
import io.netty.channel.Channel;

import java.util.ArrayList;
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
    private final Map<String, LinkedList<Address>> servicesMap;

    /**
     * 某个服务提供者注册的所有服务
     */
    private final Map<Channel, ArrayList<String>> channelServicesMap;

    public ServiceCenter() {
        servicesMap = new ConcurrentHashMap<>();
        channelServicesMap = new ConcurrentHashMap<>();
    }

    /**
     * 寻找服务
     */
    public LinkedList<Address> discovery(DiscoveryDto discoveryDto) {
        return servicesMap.get(discoveryDto.getServiceName());
    }

    /**
     * 注册服务
     */
    public void register(RegisterDto registerDto, Channel channel) {
        ArrayList<String> serviceNameList = channelServicesMap.getOrDefault(channel, new ArrayList<>());
        serviceNameList.add(registerDto.getServiceName());
        LinkedList<Address> addresses = servicesMap.get(registerDto.getServiceName());
        if (addresses == null) {
            addresses = new LinkedList<>();
        }
        addresses.add(registerDto.getAddress());
        servicesMap.put(registerDto.getServiceName(), addresses);
    }

    /**
     * 将不可用的服务移除
     */
    public void unavailable(Channel channel) {
        ArrayList<String> serviceNameList = channelServicesMap.get(channel);
        if (serviceNameList != null) {
            for (String serviceName : serviceNameList) {
                servicesMap.remove(serviceName);
            }
        }
    }

}
