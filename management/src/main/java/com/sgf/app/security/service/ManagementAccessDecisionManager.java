package com.sgf.app.security.service;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by sgf on 2018\1\31 0031.
 */
public class ManagementAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        //这段代码其实不需要,因为spring-security-core-4.1.4.RELEASE-sources.jar!/org/springframework/security/access/intercept/AbstractSecurityInterceptor.java第215行判断提前返回了,不会进入decide方法
        if (CollectionUtils.isEmpty(configAttributes)) {
            throw new AccessDeniedException("无操作权限！");
        }

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRoles = ca.getAttribute();
            for (GrantedAuthority grantedAuthority:grantedAuthorities){
                String hasRole = grantedAuthority.getAuthority();
                int flag =  needRoles.indexOf(hasRole);
                if(-1 != flag){
                    return;
                }
            }
        }

        //该url有配置权限,但是当然登录用户没有匹配到对应权限,则禁止访问
        throw new AccessDeniedException("无操作权限！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
