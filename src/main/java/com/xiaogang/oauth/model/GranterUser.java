package com.xiaogang.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ProjectName : oauth
 * @作者 : 侯小刚
 * @描述 :
 * @创建日期 : 2020/8/16 9:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GranterUser implements AuthenticatedPrincipal, Serializable {

    //昵称
    private String nickname;

    //手机号
    private String mobile;

    //头像
    private String avatar;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getName() {
        return null;
    }
}
