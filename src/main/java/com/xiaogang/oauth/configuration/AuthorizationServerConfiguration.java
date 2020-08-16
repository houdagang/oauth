package com.xiaogang.oauth.configuration;

import com.xiaogang.oauth.provider.EmailPassGranter;
import com.xiaogang.oauth.provider.TestTokenEnhancer;
import com.xiaogang.oauth.service.GranterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private ClientDetailsService jdbcClientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private TestTokenEnhancer testTokenEnhancer;

    @Autowired
    private GranterLoginService granterLoginService;

    //使用密码模式认证
   /* @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }*/

    //Jwt获取授权
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //Password 认证模式需要
        endpoints.authenticationManager(authenticationManager);
        //将token转换成Jwt
        endpoints.accessTokenConverter(jwtAccessTokenConverter);
        //授权码储存到数据库
        endpoints.authorizationCodeServices(authorizationCodeServices);
        //授权记录存储到数据库
        endpoints.approvalStore(approvalStore);
        //token 存储到数据库
        endpoints.tokenStore(tokenStore);
        /**
         * 返回扩展信息
         * 注意：这里要让testTokenEnhancer在jwtAccessTokenConverter前面
         * 不然使用jwt的话会不成功
         */
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<TokenEnhancer>();
        delegates.add(testTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(delegates);
        endpoints.tokenEnhancer(enhancerChain);

        //自定义登陆方式
        List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getAuthorizationCodeServices(),endpoints.getTokenStore(),
                endpoints.getTokenServices(),endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory());
        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
    }

    private List<TokenGranter> getTokenGranters(AuthorizationCodeServices authorizationCodeServices, TokenStore tokenStore,
                                                AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                                OAuth2RequestFactory requestFactory) {
        return new ArrayList<>(Arrays.asList(
                //默认授权码模式登陆
                new AuthorizationCodeTokenGranter(tokenServices,authorizationCodeServices,clientDetailsService,requestFactory),
                //默认password模式登陆
                new ResourceOwnerPasswordTokenGranter(authenticationManager,tokenServices,clientDetailsService,requestFactory),
                //默认client_credentials模式登陆
                new ClientCredentialsTokenGranter(tokenServices,clientDetailsService,requestFactory),
                //自定义邮箱、密码登陆
                new EmailPassGranter(granterLoginService,tokenServices,clientDetailsService,requestFactory)
        ));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //去数据库查询
        clients.withClientDetails(jdbcClientDetailsService);

        //实时查询
        /*clients
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
                .redirectUris("http://www.baidu.com");*/
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
