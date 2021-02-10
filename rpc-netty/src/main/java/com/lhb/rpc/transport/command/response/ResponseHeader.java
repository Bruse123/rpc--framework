package com.lhb.rpc.transport.command.response;

import com.lhb.rpc.transport.command.Code;
import com.lhb.rpc.transport.command.Header;
import lombok.Getter;
import lombok.Setter;

/**
 *  响应头
 * @author BruseLin
 */
@Getter
@Setter
public class ResponseHeader extends Header {

    private int responseCode;

    private String responseMsg;

    public ResponseHeader(String type, String version, String requestId,  Throwable throwable) {
        this(type, version, requestId, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(String type, String version, String requestId) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader( String type, String version, String requestId , int code, String responseMsg) {
        super(type, version, requestId);
        this.responseCode = code;
        this.responseMsg = responseMsg;
    }

}
