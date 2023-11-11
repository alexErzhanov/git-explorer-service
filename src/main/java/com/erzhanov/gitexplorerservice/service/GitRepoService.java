package com.erzhanov.gitexplorerservice.service;

import com.erzhanov.gitexplorerservice.dto.GitRepository;
import reactor.core.publisher.Flux;

public interface GitRepoService {
    /**
     * Retrieves all non-fork repositories for a given Git user and populates their branches.
     *
     * @param username The Git username for which repositories are to be fetched.
     * @return A reactive stream of Git repositories with populated branches.
     */
    Flux<GitRepository> findAllUserRepos(String username);
}
