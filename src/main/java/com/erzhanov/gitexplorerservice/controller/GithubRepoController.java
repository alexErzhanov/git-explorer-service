package com.erzhanov.gitexplorerservice.controller;

import com.erzhanov.gitexplorerservice.dto.GitRepository;
import com.erzhanov.gitexplorerservice.service.GithubRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
public class GithubRepoController {

    private final GithubRepoService githubRepoService;

    @GetMapping("/{username}")
    public Mono<ResponseEntity<List<GitRepository>>> findAllUserRepos(@PathVariable String username) {
        return githubRepoService.findAllUserRepos(username)
                .collectList()
                .map(ResponseEntity::ok);
    }
}
