package com.sgf.app.config;

import com.sgf.base.security.custom.filter.*;
import com.sgf.base.security.custom.interceptor.CustomAccessDecisionManager;
import com.sgf.base.security.custom.interceptor.CustomFilterSecurityInterceptor;
import com.sgf.base.security.custom.interceptor.CustomSecurityMetadataSource;
import com.sgf.base.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public FilterInvocationSecurityMetadataSource customSecurityMetadataSource() {
        CustomSecurityMetadataSource securityMetadataSource = new CustomSecurityMetadataSource();
        return securityMetadataSource;
    }

    @Bean
    public AccessDecisionManager customAccessDecisionManager() {
        return new CustomAccessDecisionManager();
    }


    @Bean
    CustomFilterSecurityInterceptor customFilterSecurityInterceptor(){
        CustomFilterSecurityInterceptor customFilterSecurityInterceptor = new CustomFilterSecurityInterceptor();

       customFilterSecurityInterceptor.setSecurityMetadataSource(customSecurityMetadataSource());
        customFilterSecurityInterceptor.setAccessDecisionManager( new CustomAccessDecisionManager());

        return customFilterSecurityInterceptor;
    }



    @Bean
    UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    protected CustomeAuthenticationProvider customeAuthenticationProvider() {
        CustomeAuthenticationProvider provider = new CustomeAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService());
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        provider.setPreAuthenticationChecks(new CustomePreAuthenticationChecks());

        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    protected CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler() {
        CustomSimpleUrlAuthenticationFailureHandler failureHandler = new CustomSimpleUrlAuthenticationFailureHandler("/login/backLogin?error=true");
        failureHandler.setUseForward(true);
        return failureHandler;
    }

    //配置封装 customUsernamePasswordAuthenticationFilter 的过滤器
    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        //为过滤器添加认证器
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);

        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(customSimpleUrlAuthenticationFailureHandler());
        //customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationDetailsSource(new CustomAuthenticationDetailsSource());

        return customUsernamePasswordAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(customUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customeAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

/*        http.authorizeRequests()
                .antMatchers("/security*//**").permitAll()
                .antMatchers("/imageCode/getCode").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/security/login")
                .failureHandler(customSimpleUrlAuthenticationFailureHandler())
                .successHandler(new CustomAuthenticationSuccessHandler())
                .authenticationDetailsSource(new CustomAuthenticationDetailsSource())
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().ignoringAntMatchers("/security/login");*/


        //注册customUsernamePasswordAuthenticationFilter  注意放置的顺序 这很关键
        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(authenticationManager()), RequestCacheAwareFilter.class);

        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/imageCode/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/login/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/login"))
                .and()
                .logout()
                .logoutSuccessUrl("/login/login?logout")
                .permitAll();

        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);



 /*       http.authorizeRequests()
                .antMatchers("/security*//**").permitAll()
         .antMatchers("/imageCode/getCode").permitAll()
         .anyRequest().authenticated()
         .and()
         .formLogin()
         .loginPage("/security/login")
         .failureHandler(customSimpleUrlAuthenticationFailureHandler())
         .authenticationDetailsSource(new CustomAuthenticationDetailsSource())
         .permitAll()
         .and()
         .logout().permitAll()
         .and()
         .csrf().ignoringAntMatchers("/security/login");*/


    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/plugin/**");
    }


}
