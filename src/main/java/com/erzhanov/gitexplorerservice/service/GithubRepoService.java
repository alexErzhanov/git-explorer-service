package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.client.GithubClient;
import com.erzhanov.gitexplorerservice.dto.GitHubRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GithubRepoService {

    private final GithubClient gitHubClient;

    /**
     * Retrieves all non-fork repositories for a given GitHub user and populates their branches.
     *
     * @param username The GitHub username for which repositories are to be fetched.
     * @return A reactive stream of GitHub repositories with populated branches.
     */
    public Flux<GitHubRepo> findAllUserRepos(String username) {
        return Mono.fromCallable(() -> gitHubClient.listRepos(username))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .filter(repo -> !repo.isFork())
                .flatMap(repo -> populateBranches(repo, username));
    }

    private Mono<GitHubRepo> populateBranches(GitHubRepo repo, String username) {
        return Mono.fromCallable(() -> gitHubClient.listBranches(username, repo.getName()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .doOnNext(repo::setBranches)
                .thenReturn(repo);
    }
}
