package com.sk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class IgniteApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        start(args);
    }

    public static ConfigurableApplicationContext start(String[] args) {
        return SpringApplication.run(IgniteApplication.class, args);
    }

    public static void stop(ConfigurableApplicationContext configurableApplicationContext) {
        configurableApplicationContext.close();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IgniteApplication.class);
    }
}


