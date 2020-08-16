package com.xiaogang.oauth.provider;

import com.xiaogang.oauth.model.GranterUser;
import com.xiaogang.oauth.service.GranterLoginService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/16 20:46
 */
public class EmailPassGranter extends AbstractTestDoTokenGranter{

    private static final String GRANT_TYPE = "email_password";

    private GranterLoginService granterLoginService;

    public EmailPassGranter(GranterLoginService granterLoginService, AuthorizationServerTokenServices tokenServices,
                            ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices,clientDetailsService,requestFactory,GRANT_TYPE);
        this.granterLoginService = granterLoginService;
    }

    @Override
    protected GranterUser getGranterUser(Map<String, String> parameters) {
        //String identifer = parameters.get("identifier");
        //String credential = parameters.get("credential");
        String username = parameters.get("username");
        String password = parameters.get("password");
        return granterLoginService.loadByEmailPassword(username,password);
    }
}
