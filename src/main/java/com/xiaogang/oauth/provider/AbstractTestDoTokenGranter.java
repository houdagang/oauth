package com.xiaogang.oauth.provider;

import com.xiaogang.oauth.model.GranterUser;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/16 10:23
 */
public abstract class AbstractTestDoTokenGranter extends AbstractTokenGranter {

    private final OAuth2RequestFactory requestFactory;

    protected AbstractTestDoTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.requestFactory = requestFactory;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String,String> parameters = tokenRequest.getRequestParameters();
        //模板方法
        GranterUser granterUser = getGranterUser(parameters);
        OAuth2Request storedOAuth2Request = this.requestFactory.createOAuth2Request(client,tokenRequest);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(granterUser,null,granterUser.getAuthorities());
        authentication.setDetails(granterUser);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request,authentication);
        return oAuth2Authentication;
    }

    protected abstract GranterUser getGranterUser(Map<String,String> parameters);


}
