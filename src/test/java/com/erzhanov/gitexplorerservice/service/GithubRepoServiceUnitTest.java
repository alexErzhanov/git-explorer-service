package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.client.GithubClient;
import com.erzhanov.gitexplorerservice.config.FeignConfiguration;
import com.erzhanov.gitexplorerservice.dto.GitBranch;
import com.erzhanov.gitexplorerservice.dto.GitCommit;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepoServiceUnitTest {

    @Mock
    private GithubClient githubPrimaryClient;
    @Mock
    private FeignConfiguration feignConfiguration;

    private GitRepoService repoService;

    @BeforeEach
    void setUp() {
        this.repoService = new GithubRepoService(githubPrimaryClient, feignConfiguration);
        lenient().when(feignConfiguration.getGithubUri()).thenReturn("uri");
        lenient().when(feignConfiguration.getGithubFallbackUri()).thenReturn("uri");
    }

    @Test
    void findAllUserRepos_shouldReturnReposWithoutForks() {
        // given
        when(githubPrimaryClient.listRepos(any(), anyString())).thenReturn(List.of(createRepo(1, false), createRepo(2, false), createRepo(3, true)));
        GitBranch gitBranch = givenBranch(1);
        GitBranch gitBranch1 = givenBranch(2);
        when(githubPrimaryClient.listBranches(any(), eq("user"), anyString())).thenReturn(List.of(gitBranch, gitBranch1));

        // when
        Flux<GitRepository> repoFlux = repoService.findAllUserRepos("user");

        // then
        StepVerifier.create(repoFlux)
                .consumeNextWith(repo -> {
                    assertThat(repo.getName()).isIn("repo 1", "repo 2");
                    assertThat(repo.getBranches().size()).isEqualTo(2);
                })
                .consumeNextWith(repo -> {
                    assertThat(repo.getName()).isIn("repo 1", "repo 2");
                    assertThat(repo.getBranches().size()).isEqualTo(2);
                })
                .verifyComplete();

        verify(githubPrimaryClient, times(1)).listRepos(any(), eq("user"));
        verify(githubPrimaryClient, times(2)).listBranches(any(), eq("user"), anyString());
    }

    private GitRepository createRepo(int number, boolean isFork) {
        return GitRepository.builder()
                .name("repo %d".formatted(number))
                .isFork(isFork)
                .build();
    }

    private GitBranch givenBranch(int number) {
        return GitBranch.builder()
                .name("branch %d".formatted(number))
                .commit(new GitCommit("sha%d".formatted(number)))
                .build();
    }
}