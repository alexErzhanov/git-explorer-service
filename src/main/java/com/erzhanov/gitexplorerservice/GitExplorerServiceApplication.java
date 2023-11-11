package com.erzhanov.gitexplorerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GitExplorerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitExplorerServiceApplication.class, args);
    }

}
