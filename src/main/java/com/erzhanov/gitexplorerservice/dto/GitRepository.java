package com.erzhanov.gitexplorerservice.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class GitRepository {
    private String name;
    private boolean isFork;
    private GitHubUser owner;
    private List<GitBranch> branches;
}
