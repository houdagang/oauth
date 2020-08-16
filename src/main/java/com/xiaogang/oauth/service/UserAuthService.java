package com.xiaogang.oauth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaogang.oauth.mapper.UserAuthMapper;
import com.xiaogang.oauth.model.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 16:58
 */
@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        //判断username = identifier字段
        queryWrapper.lambda().eq(UserAuth::getIdentifier,username);
        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if(null == userAuth) {
            throw new UsernameNotFoundException("账号不存在");
        }
        //权限不能为空，随便给一个权限
        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList("ROOT_USER");
        User user = new User(userAuth.getIdentifier(),userAuth.getCredential(),list);
        return user;
    }
}
