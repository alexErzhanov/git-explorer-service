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
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
public class GithubRepoController {

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
    @RequestMapping(path = "/{username}", method = RequestMethod.GET, produces = "application/json")
    public Mono<ResponseEntity<List<GitRepository>>> findAllUserRepos(
            @PathVariable String username,
            @RequestHeader(name = "Accept") String acceptHeader
    ) {
        if (!acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw new InvalidMediaTypeException(acceptHeader, "Please use application/json");
        }
        Mono<ResponseEntity<List<GitRepository>>> map = githubRepoService.findAllUserRepos(username)
                .collectList()
                .map(ResponseEntity::ok);
        System.out.println("hello");
        return map;
    }
}
