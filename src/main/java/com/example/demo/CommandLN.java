package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class CommandLN implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("before boot");
    }
}
