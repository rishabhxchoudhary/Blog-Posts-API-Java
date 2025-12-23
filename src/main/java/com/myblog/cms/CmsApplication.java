package com.myblog.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CmsApplication {

    public static void main(String[] args) {
        // 1. Load .env file
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Don't crash if .env is missing (e.g. in prod)
                .load();

        // 2. Feed env vars into System Properties so Spring can see them
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        // 3. Start Spring Boot
        SpringApplication.run(CmsApplication.class, args);
    }
}