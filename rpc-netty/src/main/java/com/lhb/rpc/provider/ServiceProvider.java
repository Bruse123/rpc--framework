package com.lhb.rpc.provider;

import com.lhb.rpc.service.RpcServiceProperties;

/**
 *
 * @author BruseLin
 */
public interface ServiceProvider {

    /**
     * 发布服务
     * @param service 服务对象
     * @param rpcServiceProperties 服务对象的相关属性
     */
void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * 根据服务名称获取服务
     * @param rpcServiceProperties 服务对象的相关属性
     * @return 服务对象
     */
Object getService(RpcServiceProperties rpcServiceProperties);

}
