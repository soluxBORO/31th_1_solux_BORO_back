package com.boro.domain.post.service.query;

import com.boro.domain.post.converter.PostConverter;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.domain.post.repository.PostRepository;
import com.boro.global.error.code.status.PostErrorCode;
import com.boro.global.error.exception.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;

    public List<PostResponseDTO.PostSummary> getPostList(PostCategory category, boolean onlyAvailable) {
        List<Post> posts = postRepository.findPostList(category, onlyAvailable);
        return posts.stream()
                .map(PostConverter::toPostSummary)
                .toList();
    }

    public PostResponseDTO.PostDetail getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .filter(p -> p.getStatus() != PostStatus.DELETED)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        return PostConverter.toPostDetail(post);
    }
}
