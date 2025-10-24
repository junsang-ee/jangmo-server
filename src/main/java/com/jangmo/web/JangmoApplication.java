package com.jangmo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class JangmoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JangmoApplication.class, args);
    }
}
