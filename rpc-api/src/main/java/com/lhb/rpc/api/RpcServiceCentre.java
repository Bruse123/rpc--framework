package com.lhb.rpc.api;

import java.io.Closeable;
import java.net.URI;

/**
 * RPC服务中心，对外提供服务的接口
 * @Author: BruseLin
 * @Date: 2021/1/30 23:11
 */
public interface RpcServiceCentre extends Closeable {

    /**
     * 客户端获取远程服务的引用
     * @param uri 远程服务地址
     * @param serviceClass 服务的接口类的Class
     * @return 远程服务引用
     * @param <T> 服务接口类型
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    /**
     * 服务端注册服务的实现实例
     * @param service 实现的实例
     * @param serviceClass 服务的接口类的Class
     * @param <T> 服务接口的类型
     * @return 服务地址
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 服务器启动RPC框架，监听接口，开始提供远程服务
     * @return 服务实例，用于程序停止的时候安全的关闭服务。
     * @throws Exception
     */
    Closeable startServer() throws Exception;
}
