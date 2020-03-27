package com.sxnsyh.backendseed.common.util;

import com.sxnsyh.backendseed.common.base.Result;
import org.springframework.http.HttpStatus;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(HttpStatus.OK)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result()
                .setCode(HttpStatus.OK)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(HttpStatus.BAD_REQUEST)
                .setMessage(message);
    }

    public static Result genFailResult(HttpStatus httpStatus,String message) {
        return new Result()
                .setCode(httpStatus)
                .setMessage(message);
    }
}
