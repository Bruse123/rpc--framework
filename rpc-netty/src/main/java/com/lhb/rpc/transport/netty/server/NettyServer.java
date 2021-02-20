package com.lhb.rpc.transport.netty.server;

import com.lhb.rpc.factory.SingletonFactory;
import com.lhb.rpc.provider.ServiceProvider;
import com.lhb.rpc.provider.ServiceProviderImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author BruseLin
 * @Date 2021/2/18 15:14
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyServer {

    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getSingleInstance(ServiceProviderImpl.class);

    @SneakyThrows
    public void start() {
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(workerGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 是否开启 TCP 底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 128)
                //按日志等级打印netty日志
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 30 秒之内没有收到客户端请求的话就关闭连接
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));

                    }
                });
        // 绑定端口，同步等待绑定成功
        ChannelFuture future = serverBootstrap.bind(host, PORT).sync();
        // 同步等待服务端监听端口关闭
        future.channel().closeFuture().sync();
    }
}
