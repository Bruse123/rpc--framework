package com.lhb.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求头
 * @author BruseLin
 */
@Data
@AllArgsConstructor
public class Header {

    /**
     * 请求id，唯一标识一个请求
     */
    private String requestId;

    /**
     * 命令类型
     */
    private String type;

    /**
     * 命令的版本号（传输协议的版本号）
     * 在设计通信协议时，让协议具备持续的升级能力，并且保持向下兼容是非常重要的。
     * 一旦传输协议发生变化，为了确保使用这个传输协议的这些程序还能正常工作，或者是向下兼容，
     * 协议中必须提供一个版本号，标识收到的这条数据使用的是哪个版本的协议。
     */
    private String version;
}
