package com.xiaogang.oauth.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 21:12
 */
public enum HttpStatus {

    UNKNOWN(-1,"未知错误"),
    SUCCESS(200,"成功"),
    FAILURE(999,"失败");

    private int code;
    private String message;

    private HttpStatus(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String,Object> map() {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",this.code);
        map.put("message",this.message);
        return map;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
