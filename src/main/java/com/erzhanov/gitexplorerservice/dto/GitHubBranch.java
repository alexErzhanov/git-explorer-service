package com.erzhanov.gitexplorerservice.dto;

import lombok.Data;

@Data
public class GitHubBranch {
    private String name;
    private GitHubCommit commit;
}
