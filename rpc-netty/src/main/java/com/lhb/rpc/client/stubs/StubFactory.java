package com.lhb.rpc.client.stubs;

import com.lhb.rpc.transport.Transport;

/**
 *
 * @author BruseLin
 */
public interface StubFactory {

    /**
     * 创建一个
     * @param transport
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
