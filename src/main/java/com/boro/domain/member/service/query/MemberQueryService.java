package com.boro.domain.member.service.query;

import com.boro.domain.member.converter.AssetConverter;
import com.boro.domain.member.converter.MemberConverter;
import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.AssetRepository;
import com.boro.domain.member.repository.MemberAssetRepository;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.member.repository.PointHistoryRepository;
import com.boro.domain.rentalrequest.entity.Review;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import com.boro.domain.rentalrequest.repository.ReviewRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewRepository reviewRepository;
    private final AssetRepository assetRepository;
    private final MemberAssetRepository memberAssetRepository;

    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponseDTO.MemberInfo getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return MemberConverter.toMemberInfo(member);
    }

    public List<MemberResponseDTO.PointHistory> getPointHistory(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return pointHistoryRepository.findByMemberOrderByCreatedAtDesc(member).stream()
                .map(MemberConverter::toPointHistoryDTO)
                .toList();
    }

    public MemberResponseDTO.Review getReceivedReviews(ReviewSentiment reviewSentiment, Long memberId) {
        Member receiver = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<Review> reviewList = reviewRepository.findByReceiverAndReviewSentimentOrderByCreatedAtDesc(receiver, reviewSentiment);

        if (reviewSentiment == ReviewSentiment.GOOD){
            Integer dislikeCnt = reviewRepository.countByReceiverAndReviewSentiment(receiver, ReviewSentiment.BAD);
            return MemberConverter.toReview(reviewList.size(), dislikeCnt, reviewList, receiver);
        } else {
            Integer likeCnt = reviewRepository.countByReceiverAndReviewSentiment(receiver, ReviewSentiment.GOOD);
            return MemberConverter.toReview(likeCnt, reviewList.size(), reviewList, receiver);
        }
    }

    public MemberResponseDTO.Review getWrittenReviews(ReviewSentiment reviewSentiment, Long memberId){
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<Review> reviewList = reviewRepository.findByWriterAndReviewSentimentOrderByCreatedAtDesc(writer, reviewSentiment);

        if (reviewSentiment == ReviewSentiment.GOOD){
            Integer dislikeCnt = reviewRepository.countByWriterAndReviewSentiment(writer, ReviewSentiment.BAD);
            return MemberConverter.toReview(reviewList.size(), dislikeCnt, reviewList, writer);
        } else {
            Integer likeCnt = reviewRepository.countByWriterAndReviewSentiment(writer, ReviewSentiment.GOOD);
            return MemberConverter.toReview(likeCnt, reviewList.size(), reviewList, writer);
        }
    }

    public List<MemberResponseDTO.StoreAsset> getStoreAssetsInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return AssetConverter.toStoreAssetList(assetRepository.findAll());
    }

    public List<MemberResponseDTO.MemberAsset> getMemberAssetsInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return memberAssetRepository.findByMember(member).stream()
                .map(MemberConverter::toMemberAsset)
                .toList();
    }

}
