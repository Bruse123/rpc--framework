package com.lhb.rpc.service;

import com.lhb.rpc.annotation.RpcService;
import com.lhb.rpc.api.Hello;
import com.lhb.rpc.api.HelloService;

/**
 * @Author BruseLin
 * @Date 2021/3/4 10:39
 * @Version 1.0
 */
@RpcService(ServiceName = "helloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public Hello hello(Hello hello) {
        System.out.println("收到客户端消息" + hello.toString());
        hello.setMessage("222");
        return hello;
    }


}
