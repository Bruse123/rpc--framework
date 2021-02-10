package com.lhb.rpc.proxy;

import com.lhb.rpc.service.RpcServiceProperties;
import com.lhb.rpc.transport.netty.NettyTransport;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author BruseLin
 * @Date 2021/2/10 15:12
 * @Version 1.0
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private final NettyTransport nettyTransport;

    private final RpcServiceProperties rpcServiceProperties;

    public RpcClientProxy(NettyTransport nettyTransport, RpcServiceProperties rpcServiceProperties) {
        this.nettyTransport = nettyTransport;
        this.rpcServiceProperties = rpcServiceProperties;
    }

    /**
     * loader：一个classloader对象，定义了由哪个classloader对象对生成的代理类进行加载
     * <p>
     * interfaces：一个interface对象数组，表示我们将要给我们的代理对象提供一组什么样的接口，
     * 如果我们提供了这样一个接口对象数组，那么也就是声明了代理类实现了这些接口，
     * 代理类就可以调用接口中声明的所有方法。
     * <p>
     * h：一个InvocationHandler对象，表示的是当动态代理对象调用方法的时候
     * 会关联到哪一个InvocationHandler对象上，并最终由其调用。
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * proxy:代理类代理的真实代理对象
     * method:我们所要调用某个对象真实的方法的Method对象
     * args:指代代理对象方法传递的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("proxy class-[{}] invoked method: [{}]", proxy.getClass().getSimpleName(), method.getName());


        return null;
    }
}
