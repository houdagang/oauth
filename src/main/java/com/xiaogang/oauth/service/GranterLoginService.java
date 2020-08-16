package com.xiaogang.oauth.service;

import com.xiaogang.oauth.model.GranterUser;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/16 9:50
 */
public interface GranterLoginService {

    GranterUser loadByEmailPassword(String identifier,String credential);

}
