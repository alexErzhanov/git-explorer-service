package com.erzhanov.gitexplorerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Git Explorer Service", version = "0.0.1"))
public class GitExplorerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitExplorerServiceApplication.class, args);
    }

}
