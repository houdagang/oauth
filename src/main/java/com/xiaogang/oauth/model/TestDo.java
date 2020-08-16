package com.xiaogang.oauth.model;

import com.xiaogang.oauth.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.util.test.Test;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 21:15
 */
@Data
@AllArgsConstructor
//如果字段为空，则返回的Json中将不会有为空的字段
public class TestDo {

    private int code;
    private String message;
    private Object data;

    public static TestDo build() {
        return build(null);
    }

    public static TestDo build(Object data) {
        return new TestDo(200,"成功",data);
    }

    public static TestDo buildFail(HttpStatus httpStatus) {
        return buildFail(httpStatus.getCode(),httpStatus.getMessage());
    }

    public static TestDo buildFail(String message) {
        return buildFail(400,message);
    }

    private static TestDo buildFail(Integer code, String message) {
        return new TestDo(code,message,null);
    }

}
