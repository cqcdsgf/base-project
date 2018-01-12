package com.sgf.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by sgf on 2018\1\12 0012.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("/secure/home");
        registry.addViewController("/secure").setViewName("/secure/home");
        registry.addViewController("/hello").setViewName("/secure/hello");
        registry.addViewController("/ip").setViewName("/secure/ipHello");
        registry.addViewController("/ipLogin").setViewName("/secure/ipLogin");

    }

}
