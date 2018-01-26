package com.sgf.app.config;

import com.sgf.app.security.service.CustomUserDetailsService;
import com.sgf.base.security.custome.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
        CustomSimpleUrlAuthenticationFailureHandler failureHandler = new CustomSimpleUrlAuthenticationFailureHandler("/security/backLogin?error=true");
        failureHandler.setUseForward(true);
        return failureHandler;
    }

    //配置封装 customUsernamePasswordAuthenticationFilter 的过滤器
/*    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        //为过滤器添加认证器
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);

        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(customSimpleUrlAuthenticationFailureHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationDetailsSource(new CustomAuthenticationDetailsSource());

        return customUsernamePasswordAuthenticationFilter;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(customUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customeAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/security/**").permitAll()
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
                .csrf().ignoringAntMatchers("/security/login");


        //注册customUsernamePasswordAuthenticationFilter  注意放置的顺序 这很关键
/*        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(authenticationManager()), RequestCacheAwareFilter.class);*/

/*        http.authorizeRequests()
                .antMatchers("/security*//**").permitAll()
                .antMatchers("/imageCode/getCode").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/security/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/security/login"))
                .and()
                .logout()
                .logoutSuccessUrl("/security/login?logout")
                .permitAll();*/



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
