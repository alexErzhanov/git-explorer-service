package com.erzhanov.gitexplorerservice.client;

import com.erzhanov.gitexplorerservice.dto.GitBranch;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;

@FeignClient(name = "githubClient")
public interface GithubClient {

    @GetMapping(path = "/users/{username}/repos")
    List<GitRepository> listRepos(URI baseUri, @PathVariable("username") String username);

    @GetMapping(path = "/repos/{owner}/{repo}/branches")
    List<GitBranch> listBranches(URI baseUri, @PathVariable("owner") String owner, @PathVariable("repo") String repo);

}
