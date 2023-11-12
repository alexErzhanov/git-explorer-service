package com.erzhanov.gitexplorerservice.controller;

import com.erzhanov.gitexplorerservice.dto.ErrorResponse;
import com.erzhanov.gitexplorerservice.dto.GitRepository;
import com.erzhanov.gitexplorerservice.service.GithubRepoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
@Slf4j
public class GithubRepoController {
    public static final String USE_APPLICATION_JSON = "Please use application/json";

    private final GithubRepoService githubRepoService;

    @Operation(
            summary = "Find all repositories for a user",
            parameters = {
                    @Parameter(name = "username", description = "GitHub username", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successful retrieval of repositories",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GitRepository.class))
                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Github user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )),
                    @ApiResponse(responseCode = "406",
                            description = "Invalid media type",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ))
            }
    )
    @GetMapping("/{username}")
    public Mono<ResponseEntity<List<GitRepository>>> findAllUserRepos(
            @PathVariable String username,
            @RequestHeader(name = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE)
            String acceptHeader
    ) {
        log.info("Received request for username: {}", username);
        if (!acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
            log.warn("Invalid media type: {}", acceptHeader);
            throw new InvalidMediaTypeException(acceptHeader, USE_APPLICATION_JSON);
        }
        return githubRepoService.findAllUserRepos(username)
                .collectList()
                .doOnSuccess(repos -> {
                    log.info("Successfully retrieved repositories for user: {}", username);
                    log.debug("List of repositories: {}", repos);
                })
                .map(ResponseEntity::ok);
    }
}
