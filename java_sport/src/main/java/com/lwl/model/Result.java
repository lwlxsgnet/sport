package com.lwl.model;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    /* 响应码 */
    private int code;
    /* 响应信息 */
    private String message;
    /* 响应数据 */
    private T data;

    /* delete update */
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /* select */
    public Result(String message, T data) {
        this.message = message;
        this.data = data;
    }

    /* update delete */
    public static <T> Result<T> success() {
        return new Result<>(200, "success");
    }
    /* select */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message);
    }
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(500, "error");
    }
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }
    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
