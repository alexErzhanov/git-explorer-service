package com.erzhanov.gitexplorerservice.client;

import com.erzhanov.gitexplorerservice.dto.GitBranch;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GithubClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{username}/repos")
    List<GitRepository> listRepos(@PathVariable("username") String username);

    @RequestMapping(method = RequestMethod.GET, value = "/repos/{owner}/{repo}/branches")
    List<GitBranch> listBranches(@PathVariable("owner") String owner, @PathVariable("repo") String repo);

}
