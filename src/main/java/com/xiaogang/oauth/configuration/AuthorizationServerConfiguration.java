package com.xiaogang.oauth.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020-08-14 15:01
 */
@Configuration
//开启授权服务器
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    //使用密码模式认证
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                //使用内存设置
                .inMemory()
                //客户端
                .withClient("client")
                //客户端密码
                .secret(passwordEncoder.encode("secret"))
                //授权类型
                //http://localhost:8888/oauth/authorize?client_id=client&scope=app&response_type=code
                //授权码模式认证，密码模式认证，客户端凭证认证
                //access_token有过期时间，可以自己指定refresh_token,注：客户端凭证拿不到
                .authorizedGrantTypes("authorization_code","password","client_credentials","refresh_token")
                .scopes("app")
                //注册回调地址
                //无论是否授权，都会跳转这个地址，如果授权成功会返回一个code       ?code=OJ8R6c
                //不授权返回 ?error=access_denied&error_description=User%20denied%20access
                .redirectUris("http://www.baidu.com");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //开启表单认证，主要是让/oauth/token支持client_id以及client_secret作登陆认证
        security.allowFormAuthenticationForClients()
                //开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                //开启/oauth/check_token验证端口认证无权限访问
                .checkTokenAccess("permitAll()");
    }

}
