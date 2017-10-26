package com.sgf.app.config;

import com.sgf.base.exception.BaseErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ExceptionConfig {

    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;
    private final ServerProperties serverProperties;

    public ExceptionConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public BaseErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new BaseErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }

}
