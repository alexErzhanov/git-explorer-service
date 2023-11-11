package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.client.GithubClient;
import com.erzhanov.gitexplorerservice.config.FeignConfiguration;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class GithubRepoService implements GitRepoService {

    private final GithubClient githubClient;
    private final FeignConfiguration feignConfiguration;

    /**
     * Retrieves all non-fork repositories for a given GitHub user and populates their branches.
     *
     * @param username The GitHub username for which repositories are to be fetched.
     * @return A reactive stream of GitHub repositories with populated branches.
     */
    public Flux<GitRepository> findAllUserRepos(String username) {
        URI primaryUri = URI.create(feignConfiguration.getGithubUri());
        return callPrimaryOrFallbackClient(primaryUri, username, true);
    }

    private Flux<GitRepository> callPrimaryOrFallbackClient(URI baseUri, String username, boolean usePrimary) {
        return fetchRepositoriesWithBranches(baseUri, username)
                .onErrorResume(e -> {
                    if (usePrimary) {
                        URI fallbackUri = URI.create(feignConfiguration.getGithubFallbackUri());
                        return callPrimaryOrFallbackClient(fallbackUri, username, false);
                    } else {
                        return Flux.error(e);
                    }
                });
    }

    private Flux<GitRepository> fetchRepositoriesWithBranches(URI baseUri, String username) {
        return Mono.fromCallable(() -> githubClient.listRepos(baseUri, username))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .filter(repo -> !repo.isFork())
                .flatMap(repo -> populateBranches(baseUri, repo, username));
    }

    private Mono<GitRepository> populateBranches(URI baseUri, GitRepository repo, String username) {
        return Mono.fromCallable(() -> githubClient.listBranches(baseUri, username, repo.getName()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .doOnNext(repo::setBranches)
                .thenReturn(repo);
    }
}
