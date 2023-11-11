package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GithubRepoServiceIT {
    public static final String USERNAME = "alexErzhanov";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFindAllUserReposIntegration() {
        webTestClient.get()
                .uri("/api/v1/repos/{username}", USERNAME)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GitRepository.class)
                .consumeWith(response -> {
                    var repos = response.getResponseBody();
                    Assertions.assertThat(repos).isNotEmpty().hasSize(5);
                });
    }
}
