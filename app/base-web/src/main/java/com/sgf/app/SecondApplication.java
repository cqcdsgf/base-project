package com.sgf.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by sgf on 2018\1\25 0025.
 * 测试时使用：用于模拟多个服务器(先启动一个服务器后，修改了端口号，再启动此程序)
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.sgf.base,com.sgf.app")
public class SecondApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseWebApplication.class, args);
    }
}
