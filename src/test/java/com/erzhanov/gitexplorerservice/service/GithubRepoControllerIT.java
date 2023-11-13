package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GithubRepoControllerIT {
    public static final String USERNAME = "alexErzhanov";
    public static final String INVALID_USERNAME = "alexErzhanov1";
    public static final int CURRENT_AMOUNT_OF_PUBLIC_REPOS = 6;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFindAllUserRepos_success() {
        webTestClient.get()
                .uri("/api/v1/repos/{username}", USERNAME)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GitRepository.class)
                .consumeWith(response -> {
                    var repos = response.getResponseBody();
                    Assertions.assertThat(repos).isNotEmpty().hasSize(CURRENT_AMOUNT_OF_PUBLIC_REPOS);
                });
    }

    @Test
    void testFindAllUserRepos_InvalidUsername() {
        webTestClient.get()
                .uri("/api/v1/repos/{username}", INVALID_USERNAME)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testFindAllUserRepos_InvalidMediaType() {
        webTestClient.get()
                .uri("/api/v1/repos/{username}", INVALID_USERNAME)
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }
}
