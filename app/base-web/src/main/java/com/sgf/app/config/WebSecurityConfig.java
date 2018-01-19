package com.sgf.app.config;

import com.sgf.app.security.custome.CustomSimpleUrlAuthenticationFailureHandler;
import com.sgf.app.security.custome.CustomeAuthenticationProvider;
import com.sgf.app.security.custome.CustomePreAuthenticationChecks;
import com.sgf.app.security.service.CustomUserDetailsService;
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


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(customUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customeAuthenticationProvider());


    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/security/login").permitAll()
                .antMatchers("/security/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/security/login")
                .failureHandler(new CustomSimpleUrlAuthenticationFailureHandler("/security/login?error=true"))
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().ignoringAntMatchers("/security/login");

    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/plugin/**");
    }


}
