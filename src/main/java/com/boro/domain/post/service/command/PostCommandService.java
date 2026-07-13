package com.boro.domain.post.service.command;

import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.post.converter.PostConverter;
import com.boro.domain.post.dto.request.PostRequestDTO;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.PostLike;
import com.boro.domain.post.repository.PostLikeRepository;
import com.boro.domain.post.repository.PostRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.code.status.PostErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import com.boro.global.error.exception.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    public PostResponseDTO.CreatePost createPost(Long memberId, PostRequestDTO.CreatePost request) {
        if (request.rentalEndTime().isBefore(request.rentalStartTime())) {
            throw new PostException(PostErrorCode.INVALID_RENTAL_PERIOD);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Post post = PostConverter.toPost(member, request.category());
        Item item = PostConverter.toItem(request);
        post.assignItem(item);

        Post savedPost = postRepository.save(post);
        return PostConverter.toCreatePost(savedPost);
    }

    public void updatePost(Long memberId, Long postId, PostRequestDTO.EditPost request) {
        if (request.rentalEndTime().isBefore(request.rentalStartTime())) {
            throw new PostException(PostErrorCode.INVALID_RENTAL_PERIOD);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.NOT_POST_OWNER);
        }

        PostConverter.updateItem(post.getItem(), request);
        post.updatePostCategory(request.category());
    }

    public void deletePost(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.NOT_POST_OWNER);
        }

        post.markAsDeleted();
    }

    public PostResponseDTO.LikeResult toggleLike(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        boolean liked = postLikeRepository.findByPostAndMember(post, member)
                .map(existingLike -> {
                    postLikeRepository.delete(existingLike);
                    return false;
                })
                .orElseGet(() -> {
                    postLikeRepository.save(PostLike.builder().post(post).member(member).build());
                    return true;
                });

        long likeCount = postLikeRepository.countByPost(post);
        return PostResponseDTO.LikeResult.builder()
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }
}
