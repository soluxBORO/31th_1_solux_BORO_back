package com.boro.domain.post.controller;

import com.boro.domain.post.dto.request.PostRequestDTO;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.service.command.PostCommandService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@RestController
@Tag(name = "게시글 API")
public class PostController {

    private final PostCommandService postCommandService;

    @Operation(summary = "물품 게시글 작성 API", description = "물품 대여 게시글을 작성하는 API")
    @PostMapping
    public ApiResponse<PostResponseDTO.CreatePost> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PostRequestDTO.CreatePost request
    ) {
        PostResponseDTO.CreatePost response = postCommandService.createPost(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(response);
    }
}
