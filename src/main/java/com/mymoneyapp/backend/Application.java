package com.mymoneyapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@SuppressWarnings("PMD")
public class Application implements WebMvcConfigurer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
