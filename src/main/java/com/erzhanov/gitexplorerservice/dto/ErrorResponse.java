package com.erzhanov.gitexplorerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private Integer status;
    @JsonProperty("Message")
    private String message;
}
