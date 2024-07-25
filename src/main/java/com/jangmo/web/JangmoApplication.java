package com.jangmo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JangmoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JangmoApplication.class, args);
    }
}
