package com.erzhanov.gitexplorerservice.client;

import com.erzhanov.gitexplorerservice.dto.GitHubBranch;
import com.erzhanov.gitexplorerservice.dto.GitHubRepo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GithubClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{username}/repos")
    List<GitHubRepo> listRepos(@PathVariable("username") String username);

    @RequestMapping(method = RequestMethod.GET, value = "/repos/{owner}/{repo}/branches")
    List<GitHubBranch> listBranches(@PathVariable("owner") String owner, @PathVariable("repo") String repo);

}
