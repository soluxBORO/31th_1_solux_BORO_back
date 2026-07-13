package com.boro.domain.post.controller;

import com.boro.domain.post.dto.request.PostRequestDTO;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.service.command.PostCommandService;
import com.boro.domain.post.service.query.PostQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@RestController
@Tag(name = "게시글 API")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Operation(summary = "물품 게시글 작성 API", description = "물품 대여 게시글을 작성하는 API")
    @PostMapping
    public ApiResponse<PostResponseDTO.CreatePost> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PostRequestDTO.CreatePost request
    ) {
        PostResponseDTO.CreatePost response = postCommandService.createPost(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "물품 게시글 수정 API", description = "물품 대여 게시글을 수정하는 API")
    @PatchMapping
    public ApiResponse<Void> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long postId,
            @RequestBody @Valid PostRequestDTO.EditPost request
    ) {
        postCommandService.updatePost(customUserDetails.getMemberId(), postId, request);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "물품 게시글 리스트 조회 API", description = "전체 물품 대여 게시글을 카드형 리스트로 조회하는 API")
    @GetMapping
    public ApiResponse<List<PostResponseDTO.PostSummary>> getPostList(
            @RequestParam(required = false) PostCategory category,
            @RequestParam(defaultValue = "false") boolean onlyAvailable
    ) {
        List<PostResponseDTO.PostSummary> response = postQueryService.getPostList(category, onlyAvailable);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "물품 게시글 상세 조회 API", description = "물품 대여 게시글의 상세 정보를 조회하는 API")
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO.PostDetail> getPostDetail(@PathVariable Long postId) {
        PostResponseDTO.PostDetail response = postQueryService.getPostDetail(postId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "물품 게시글 삭제 API", description = "물품 대여 게시글을 삭제하는 API")
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(customUserDetails.getMemberId(), postId);
        return ApiResponse.onSuccess(null);
    }
}
