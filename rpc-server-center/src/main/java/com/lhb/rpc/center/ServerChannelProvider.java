package com.lhb.rpc.center;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author BruseLin
 * @Date 2021/2/23 14:31
 * @Version 1.0
 */
public class ServerChannelProvider {

    private final Map<String, Channel> channelMap;

    public ServerChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    public void put(Channel channel, InetSocketAddress inetSocketAddress) {
        channelMap.put(inetSocketAddress.toString(), channel);
    }

    public void remove(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
    }

}
