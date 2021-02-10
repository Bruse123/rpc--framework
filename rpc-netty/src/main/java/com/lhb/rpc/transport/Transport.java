package com.lhb.rpc.transport;

import com.lhb.rpc.spi.Spi;
import com.lhb.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 通信接口
 * @author BruseLin
 */
@Spi
public interface Transport {

    /**
     * 发送请求
     * @param request 请求信息
     * @return 响应结果
     */
    CompletableFuture<Command> send(Command request);
}
