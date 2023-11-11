package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.client.GithubClient;
import com.erzhanov.gitexplorerservice.dto.GitBranch;
import com.erzhanov.gitexplorerservice.dto.GitCommit;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
    private GithubClient githubClient;

    private GitRepoService repoService;

    @BeforeEach
    void setUp() {
        this.repoService = new GithubRepoService(githubClient);
    }

    @Test
    void findAllUserRepos_shouldReturnReposWithoutForks() {
        // given
        when(githubClient.listRepos(anyString())).thenReturn(List.of(createRepo(1, false), createRepo(2, false), createRepo(3, true)));
        GitBranch gitBranch = givenBranch(1);
        GitBranch gitBranch1 = givenBranch(2);
        when(githubClient.listBranches(eq("user"), anyString())).thenReturn(List.of(gitBranch, gitBranch1));

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

        verify(githubClient, times(1)).listRepos("user");
        verify(githubClient, times(2)).listBranches(eq("user"), anyString());
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