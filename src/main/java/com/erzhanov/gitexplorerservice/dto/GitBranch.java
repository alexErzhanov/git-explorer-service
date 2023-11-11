package com.erzhanov.gitexplorerservice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class GitBranch {
    private String name;
    private GitCommit commit;
}
