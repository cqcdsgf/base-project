package com.sgf.base.security.custom.interceptor;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sgf on 2018\1\31 0031.
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

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
//        String httpMethod = fi.getRequest().getMethod();
        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            if(antPathMatcher.match(entry.getKey(),url)){
                return SecurityConfig.createList(entry.getValue());
            }
        }
        //没有匹配到,默认是要登录才能访问
        //return SecurityConfig.createList("ROLE_USER");
        return null;
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
