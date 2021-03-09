package com.lhb.rpc;

import com.lhb.rpc.annotation.RpcScan;
import com.lhb.rpc.transport.netty.server.NettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author BruseLin
 * @Date 2021/3/4 10:40
 * @Version 1.0
 */
@RpcScan(basePackage = {"com.lhb.rpc"})
public class NettyServerMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);

        NettyServer nettyServer = (NettyServer) applicationContext.getBean("nettyServer");
        nettyServer.start();
        /*try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
            System.out.println(inetSocketAddress.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
    }
}
