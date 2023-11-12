package com.erzhanov.gitexplorerservice.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString(of = {"name"})
public class GitRepository {
    private String name;
    private boolean fork;
    private GitHubUser owner;
    private List<GitBranch> branches;
}
