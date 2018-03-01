package com.sgf.app.home.web;

import com.sgf.base.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by sgf on 2018/2/25.
 */
public class JWTAuthenticationFilter extends GenericFilterBean {
    @Autowired
    TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain filterChain)
            throws IOException, ServletException {
        if(null == tokenService){
            tokenService = SpringContextUtil.getBean("tokenService");
        }

        /*Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request);*/

        Authentication authentication = tokenService.getAuthentication((HttpServletRequest)request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
