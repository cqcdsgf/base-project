package com.sgf.app.security.service;

import com.google.common.collect.Sets;
import com.sgf.app.domain.AuthPermission;
import com.sgf.app.domain.AuthRole;
import com.sgf.app.permission.AuthPermissionDao;
import com.sgf.base.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sgf on 2018\1\31 0031.
 */
public class ManagementSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    AuthPermissionDao authPermissionDao;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String,String> urlRoleMap = new HashMap<String,String>(){{
/*        put("/","ROLE_ANONYMOUS");
        put("/login*//**","ROLE_ANONYMOUS");
        put("/register*//**","ROLE_ANONYMOUS");
        put("/upload*//**","ROLE_ANONYMOUS");
        put("/imageCode*//**","ROLE_ANONYMOUS");*/

        put("/demo/demoValidate","ROLE_ADMIN");
        put("/demo/demoDate","ROLE_USER");
       // put("/demo/**","ROLE_USER");
    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        if(null == authPermissionDao) {
            authPermissionDao = SpringContextUtil.getBean("authPermissionDao");
        }

        Set<ConfigAttribute> configAttributes = Sets.newHashSet();
        String tempRole = "";

        AuthPermission authPermission = authPermissionDao.findByUrl(url);
        if(null == authPermission){
            //没有匹配到
            return null;
        }else{
            Set<AuthRole> roles = authPermission.getRoles();
            if(roles.size()==0){
                tempRole += tempRole.concat("403");
            }
            for(AuthRole role:roles){
                String name = role.getName();
                tempRole += tempRole.concat(",").concat(name);
            }
        }
        configAttributes.add(new SecurityConfig(tempRole));
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
