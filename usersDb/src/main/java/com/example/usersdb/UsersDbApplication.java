package com.example.usersdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class UsersDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersDbApplication.class, args);
    }

}
