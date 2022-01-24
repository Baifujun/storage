package com.baixs.storage.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author baifujun
 * @date 2021-09-08
 * @time 21:13
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public JSONObject handleGlobalException(Exception e) {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("code", 400);
        response.put("msg", "处理失败");
        response.put("data", e.getMessage());
        return response;
    }
}
