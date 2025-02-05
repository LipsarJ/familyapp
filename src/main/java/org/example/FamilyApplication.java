package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FamilyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FamilyApplication.class, args);
    }
}