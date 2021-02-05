package com.lhb.rpc.transport.command;

import lombok.Data;

/**
 *  响应头
 * @author BruseLin
 */
@Data
public class ResponseHeader extends Header {

    private int responseCode;

    private String responseMsg;

}
