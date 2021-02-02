package com.lhb.rpc.api;

import java.io.IOException;
import java.net.URI;

/**
 * @Author: BruseLin
 * @Date: 2021/1/30 23:31
 */
public interface NameService {

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param uri 服务地址
     */
    void registerService(String serviceName, URI uri) throws IOException;

    /**
     * 查找服务地址
     * @param serviceName 服务名称
     * @return 服务地址
     */
    URI findService(String serviceName) throws IOException;
}
