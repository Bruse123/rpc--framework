package com.lhb.rpc.center;

/**
 * @Author BruseLin
 * @Date 2021/3/4 11:47
 * @Version 1.0
 */
public class RegisterCenterMain {
    public static void main(String[] args) {
        RegisterCenterServer registerCenterServer = new RegisterCenterServer();
        registerCenterServer.start();
    }
}
