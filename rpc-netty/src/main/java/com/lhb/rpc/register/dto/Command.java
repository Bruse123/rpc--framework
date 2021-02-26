package com.lhb.rpc.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author BruseLin
 * @Date 2021/2/23 17:30
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command<T> {
    private String requestId;

    T data;
}
