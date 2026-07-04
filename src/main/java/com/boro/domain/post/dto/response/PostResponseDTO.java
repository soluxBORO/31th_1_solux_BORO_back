package com.boro.domain.post.dto.response;

import lombok.Builder;

public record PostResponseDTO() {

    @Builder
    public record CreatePost(
            Long postId
    ) {}
}
