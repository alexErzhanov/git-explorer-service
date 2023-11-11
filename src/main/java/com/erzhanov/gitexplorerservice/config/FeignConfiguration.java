package com.erzhanov.gitexplorerservice.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FeignConfiguration {

    @Value("${github.base-uri}")
    private String githubUri;

    @Value("${github.fallback-uri}")
    private String githubFallbackUri;

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder decoder() {
        return new JacksonDecoder();
    }
}
