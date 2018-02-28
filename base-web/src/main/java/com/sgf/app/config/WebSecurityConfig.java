package com.sgf.app.config;

import com.sgf.app.home.web.JWTAuthenticationFilter;
import com.sgf.app.home.web.JWTLoginFilter;
import com.sgf.app.home.web.WebAuthenticationProvider;
import com.sgf.base.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
        http.addFilterBefore(new JWTLoginFilter("/login/login", authenticationManager()),UsernamePasswordAuthenticationFilter.class);

        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                .antMatchers("/").permitAll()
                .antMatchers("/login/login").permitAll()
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/login/login?logout");


                // 添加一个过滤器验证其他请求的Token是否合法
        http.addFilterBefore(new JWTAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        WebAuthenticationProvider webAuthenticationProvider = new WebAuthenticationProvider();
        webAuthenticationProvider.setUserDetailsService(customUserDetailsService());
        webAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        // 使用自定义身份验证组件
        auth.authenticationProvider(webAuthenticationProvider);

    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/plugin/**");
        web.ignoring().antMatchers("/favicon.ico");
    }


}
