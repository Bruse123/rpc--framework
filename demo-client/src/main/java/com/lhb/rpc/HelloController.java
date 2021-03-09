package com.lhb.rpc;

import com.lhb.rpc.annotation.RpcReference;
import com.lhb.rpc.api.Hello;
import com.lhb.rpc.api.HelloService;
import org.springframework.stereotype.Component;

/**
 * @Author BruseLin
 * @Date 2021/3/4 10:26
 * @Version 1.0
 */
@Component
public class HelloController {

    @RpcReference(ServiceName = "helloService")
    HelloService helloService;

    public void test() {
        Hello hello = new Hello("I am client!");
        Hello response = helloService.hello(hello);
        if (response != null) {
            System.out.println("收到服务端消息：" + response.toString());
        }
    }
}
