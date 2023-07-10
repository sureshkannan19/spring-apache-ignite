package com.sk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringwebApplication {

	public static void main(String[] args) {
		start(args);
	}

	public static ConfigurableApplicationContext start(String[] args) {
		return SpringApplication.run(SpringwebApplication.class, args);
	}

	public static void stop(ConfigurableApplicationContext configurableApplicationContext) {
		configurableApplicationContext.close();
	}

}
