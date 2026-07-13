package com.boro.domain.emptyspot.service.command;

import com.boro.domain.emptyspot.converter.EmptySpotConverter;
import com.boro.domain.emptyspot.dto.request.EmptySpotRequestDTO;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.emptyspot.repository.EmptySpotRepository;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.post.converter.PostConverter;
import com.boro.domain.post.entity.EmptySpot;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.repository.PostRepository;
import com.boro.global.error.code.status.EmptySpotErrorCode;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.EmptySpotException;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EmptySpotCommandService {

    private static final long MAX_CHECKOUT_MINUTES = 5;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EmptySpotRepository emptySpotRepository;

    public EmptySpotResponseDTO.EmptySpotDetail createEmptySpot(Long memberId, EmptySpotRequestDTO.CreateEmptySpot request) {
        validateCheckoutTime(request.expectedCheckoutTime());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Post post = PostConverter.toPost(member, PostCategory.EMPTY_SPOTS);
        EmptySpot emptySpot = EmptySpotConverter.toEmptySpot(request);
        post.assignEmptySpot(emptySpot);

        Post savedPost = postRepository.save(post);
        return EmptySpotConverter.toEmptySpotDetail(savedPost);
    }

    public void deleteEmptySpot(Long memberId, Long emptySpotId) {
        EmptySpot emptySpot = emptySpotRepository.findById(emptySpotId)
                .orElseThrow(() -> new EmptySpotException(EmptySpotErrorCode.EMPTY_SPOT_NOT_FOUND));

        Post post = emptySpot.getPost();
        if (!post.getMember().getId().equals(memberId)) {
            throw new EmptySpotException(EmptySpotErrorCode.NOT_EMPTY_SPOT_OWNER);
        }

        post.markAsDeleted();
    }

    private void validateCheckoutTime(LocalDateTime expectedCheckoutTime) {
        LocalDateTime now = LocalDateTime.now();
        if (expectedCheckoutTime.isBefore(now) || expectedCheckoutTime.isAfter(now.plusMinutes(MAX_CHECKOUT_MINUTES))) {
            throw new EmptySpotException(EmptySpotErrorCode.INVALID_CHECKOUT_TIME);
        }
    }
}
