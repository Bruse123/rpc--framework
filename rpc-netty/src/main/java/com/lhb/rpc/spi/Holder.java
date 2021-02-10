package com.lhb.rpc.spi;

import lombok.Data;

/**
 * @Author BruseLin
 * @Date 2021/2/10 9:53
 * @Version 1.0
 */
@Data
public class Holder<T> {
    /**
     *
     */
    private volatile T value;
}
