package com.erzhanov.gitexplorerservice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class GitCommit {
    private String sha;
}
