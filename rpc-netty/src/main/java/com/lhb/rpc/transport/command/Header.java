package com.lhb.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求头
 * @author BruseLin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    /**
     * 请求id，唯一标识一个请求
     */
    private String requestId;

}
