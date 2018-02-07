package com.sgf.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sgf.base", "com.sgf.app"})
public class BaseWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseWebApplication.class, args);
    }
}
