package com.xiaogang.oauth.provider;

import com.xiaogang.oauth.model.GranterUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 20:57
 */
public class TestTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Object user = oAuth2Authentication.getPrincipal();
        //自定义登陆方式
        if(user instanceof GranterUser) {
            GranterUser granterUser = (GranterUser) user;
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("NickName",granterUser.getNickname());
            map.put("mobile",granterUser.getMobile());
            map.put("avatar",granterUser.getAvatar());
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(map);
            return oAuth2AccessToken;
        }

        User defaultUser = (User) user;
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        //扩展两个字段在返回的token信息里面
        map.put("client_id", oAuth2Authentication.getOAuth2Request().getClientId());
        map.put("username",defaultUser.getUsername());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(map);
        return oAuth2AccessToken;
    }
}
