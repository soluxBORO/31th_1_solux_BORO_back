package com.boro.domain.post.service.query;

import com.boro.domain.post.converter.PostConverter;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.domain.post.repository.PostLikeRepository;
import com.boro.domain.post.repository.PostRepository;
import com.boro.global.error.code.status.PostErrorCode;
import com.boro.global.error.exception.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public List<PostResponseDTO.PostSummary> getPostList(PostCategory category, boolean onlyAvailable, Long memberId) {
        List<Post> posts = postRepository.findPostList(category, onlyAvailable);
        if (posts.isEmpty()) {
            return List.of();
        }
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        Map<Long, Long> likeCounts = postLikeRepository.countGroupByPostIds(postIds).stream()
                .collect(Collectors.toMap(
                        PostLikeRepository.PostLikeCountView::getPostId,
                        PostLikeRepository.PostLikeCountView::getLikeCount
                ));
        Set<Long> likedPostIds = new HashSet<>(postLikeRepository.findLikedPostIds(postIds, memberId));

        return posts.stream()
                .map(post -> PostConverter.toPostSummary(
                        post,
                        likeCounts.getOrDefault(post.getId(), 0L),
                        likedPostIds.contains(post.getId())
                ))
                .toList();
    }

    public PostResponseDTO.PostDetail getPostDetail(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .filter(p -> p.getStatus() != PostStatus.DELETED)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        long likeCount = postLikeRepository.countByPost(post);
        boolean liked = postLikeRepository.existsByPost_IdAndMember_Id(postId, memberId);

        return PostConverter.toPostDetail(post, likeCount, liked);
    }
}
