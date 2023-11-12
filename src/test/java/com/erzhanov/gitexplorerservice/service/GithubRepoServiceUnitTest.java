package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.client.GithubClient;
import com.erzhanov.gitexplorerservice.config.FeignConfiguration;
import com.erzhanov.gitexplorerservice.dto.GitBranch;
import com.erzhanov.gitexplorerservice.dto.GitCommit;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepoServiceUnitTest {

    public static final String USER = "user";
    public static final String FIRST_REPO = "repo 1";
    public static final String SECOND_REPO = "repo 2";
    public static final String PRIMARY_URI = "primary";
    public static final String FALLBACK_URI = "fallback";
    @Mock
    private GithubClient githubClient;
    @Mock
    private FeignConfiguration feignConfiguration;

    private GitRepoService repoService;

    @BeforeEach
    void setUp() {
        this.repoService = new GithubRepoService(githubClient, feignConfiguration);
        lenient().when(feignConfiguration.getGithubUri()).thenReturn(PRIMARY_URI);
        lenient().when(feignConfiguration.getGithubFallbackUri()).thenReturn(FALLBACK_URI);
    }

    @Test
    void findAllUserRepos_shouldReturnReposWithoutForks() {
        // given
        when(githubClient.listRepos(any(), anyString())).thenReturn(List.of(createRepo(1, false), createRepo(2, false), createRepo(3, true)));
        GitBranch gitBranch = givenBranch(1);
        GitBranch gitBranch1 = givenBranch(2);
        when(githubClient.listBranches(any(), eq(USER), anyString())).thenReturn(List.of(gitBranch, gitBranch1));

        // when
        Flux<GitRepository> repoFlux = repoService.findAllUserRepos(USER);

        // then
        StepVerifier.create(repoFlux)
                .consumeNextWith(repo -> {
                    assertThat(repo.getName()).isIn(FIRST_REPO, SECOND_REPO);
                    assertThat(repo.getBranches().size()).isEqualTo(2);
                })
                .consumeNextWith(repo -> {
                    assertThat(repo.getName()).isIn(FIRST_REPO, SECOND_REPO);
                    assertThat(repo.getBranches().size()).isEqualTo(2);
                })
                .verifyComplete();

        verify(githubClient, times(1)).listRepos(any(), eq(USER));
        verify(githubClient, times(2)).listBranches(any(), eq(USER), anyString());
    }

    @Test
    void findAllUserRepos_usingFallbackURI() {
        // given
        URI primaryUri = URI.create(PRIMARY_URI);
        URI fallbackUri = URI.create(FALLBACK_URI);
        when(githubClient.listRepos(eq(primaryUri), anyString())).thenThrow(FeignException.TooManyRequests.class);
        when(githubClient.listRepos(eq(fallbackUri), anyString())).thenReturn(List.of(createRepo(1, false)));
        GitBranch gitBranch = givenBranch(1);
        GitBranch gitBranch1 = givenBranch(2);
        when(githubClient.listBranches(any(), eq(USER), anyString())).thenReturn(List.of(gitBranch, gitBranch1));

        // when
        Flux<GitRepository> repoFlux = repoService.findAllUserRepos(USER);

        // then
        StepVerifier.create(repoFlux)
                .consumeNextWith(repo -> {
                    assertThat(repo.getName()).isEqualTo(FIRST_REPO);
                    assertThat(repo.getBranches().size()).isEqualTo(2);
                })
                .verifyComplete();

        verify(githubClient, times(2)).listRepos(any(), eq(USER));
        verify(githubClient, times(1)).listBranches(any(), eq(USER), anyString());
    }

    @Test
    void findAllUserRepos_APIAreNotAvailable() {
        // given
        String expectedErrorMessage = "Server not responding";
        when(githubClient.listRepos(any(), anyString())).thenThrow(new RuntimeException(expectedErrorMessage));

        // when
        Flux<GitRepository> result = repoService.findAllUserRepos(USER);

        // then
        StepVerifier.create(result)
                .expectErrorMessage(expectedErrorMessage)
                .verify();

        verify(githubClient, times(2)).listRepos(any(), eq(USER));
    }

    private GitRepository createRepo(int number, boolean isFork) {
        return GitRepository.builder()
                .name("repo %d".formatted(number))
                .fork(isFork)
                .build();
    }

    private GitBranch givenBranch(int number) {
        return GitBranch.builder()
                .name("branch %d".formatted(number))
                .commit(new GitCommit("sha%d".formatted(number)))
                .build();
    }
}