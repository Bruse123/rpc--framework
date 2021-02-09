package com.lhb.rpc.provider;

/**
 *
 * @author BruseLin
 */
public interface ServiceProvider {

    /**
     * 发布服务
     * @param service 服务对象
     * @param serviceName 服务名称
     */
void publishService(Object service, String serviceName);

    /**
     * 根据服务名称获取服务
     * @param serviceName 服务名称
     */
void getService(String serviceName);

}
