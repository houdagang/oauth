package com.xiaogang.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaogang.oauth.mapper.UserAuthMapper;
import com.xiaogang.oauth.model.GranterUser;
import com.xiaogang.oauth.model.UserAuth;
import com.xiaogang.oauth.service.GranterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/16 20:37
 */
@Service
public class GranterLoginServiceImpl implements GranterLoginService {

    //邮箱密码登陆
    private final String EMAIL_CODE_TYPE = "02";

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Override
    public GranterUser loadByEmailPassword(String identifier, String credential) {
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAuth::getIdentityType,EMAIL_CODE_TYPE)
                .eq(UserAuth::getIdentifier,identifier);
        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if(userAuth == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        //校验密码
        boolean flag = passwordEncoder.matches(credential,userAuth.getCredential());
        if(flag) {
            GranterUser granterUser = new GranterUser();
            granterUser.setNickname("半瓶假酒");
            granterUser.setMobile("18253101170");
            granterUser.setAvatar("https://www.avatar.com/avatar.png");
            granterUser.setAuthorities(Collections.emptyList());
            return granterUser;
        } else {
            return new GranterUser();
        }
    }
}
