package com.xiaogang.oauth.controller;

import com.xiaogang.oauth.model.TestDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/15 21:31
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @GetMapping("/token")
    public TestDo getAccessToken(Principal principal, @RequestParam Map<String,String> parameters) throws HttpRequestMethodNotSupportedException {
        return tokenInfo(tokenEndpoint.getAccessToken(principal,parameters).getBody());
    }

    @PostMapping("/token")
    public TestDo postAccessToken(Principal principal, @RequestParam Map<String,String> parameters) throws HttpRequestMethodNotSupportedException {
        return tokenInfo(tokenEndpoint.postAccessToken(principal,parameters).getBody());
    }

    //只返回扩展的token信息
    private TestDo tokenInfo(OAuth2AccessToken oAuth2AccessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
        //移除jti信息
        token.getAdditionalInformation().remove("jti");
        Map<String,Object> data = new LinkedHashMap(token.getAdditionalInformation());
        data.put("accessToken",token.getValue());
        return TestDo.build(data);
    }
}
