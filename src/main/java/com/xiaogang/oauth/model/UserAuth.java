package com.xiaogang.oauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 16:36
 */
@Data
@TableName("user_auths")
public class UserAuth {

    private String userId;
    private String identityType;
    private String identifier;
    private String credential;

}
