package com.lhb.rpc;

import com.lhb.rpc.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author BruseLin
 * @Date 2021/3/4 10:36
 * @Version 1.0
 */
@RpcScan(basePackage = {"com.lhb.rpc"})
public class NettyClientMain {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        for (int i = 0; i < 3; i++) {
            helloController.test();
            Thread.sleep(5000);
        }
        /*RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName("asd")
                .methodName("test")
                .paramTypes(null)
                .arguments(null)
                .requestId(UUID.randomUUID().toString())
                .build();
        System.out.println(rpcRequest.toString());*/
    }
}
