package com.lhb.rpc.serializer.impl;

import com.lhb.rpc.client.stubs.Metadata;
import com.lhb.rpc.client.stubs.RpcRequest;

/**
 * 序列化实现类支持的对象类型（自定义）
 *
 * @author BruseLin
 */
public class Type {
    /**
     * {@link String}对象类型
     */
    final static int STRING_TYPE = 1;
    /**
     * {@link Metadata} 对象类型
     */
    final static int METADATA_TYPE = 2;
    /**
     * {@link RpcRequest} 对象类型
     */
    final static int RPC_REQUEST_TYPE = 3;



}
