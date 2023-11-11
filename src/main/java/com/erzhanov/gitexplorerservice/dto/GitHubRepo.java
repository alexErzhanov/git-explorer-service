package com.erzhanov.gitexplorerservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class GitHubRepo {
    private String name;
    private boolean fork;
    private GitHubUser owner;
    private List<GitHubBranch> branches;
}
