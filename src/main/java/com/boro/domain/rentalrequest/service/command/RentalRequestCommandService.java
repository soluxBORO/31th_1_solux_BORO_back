package com.boro.domain.rentalrequest.service.command;

import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.enums.PointReason;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.converter.RentalRequestConverter;
import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.Review;
import com.boro.domain.rentalrequest.entity.enums.Decide;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import com.boro.domain.rentalrequest.repository.RentalRequestRepository;
import com.boro.domain.rentalrequest.repository.ReviewRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.code.status.RentalRequestErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import com.boro.global.error.exception.handler.RentalRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalRequestCommandService {

    private final RentalRequestRepository rentalRequestRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RentalRequestResponseDTO.CreatedRentalRequest createRentalRequest(Post post, Member member){
        validateRentalRequest(post, member);
        RentalRequest rentalRequest = RentalRequestConverter.toRentalRequest(member, post);
        RentalRequest saved = rentalRequestRepository.save(rentalRequest);
        return RentalRequestConverter.toCreatedRentalRequest(saved);
    }

    public RentalRequestResponseDTO.DecisionResult decide(Long memberId, Long rentalId, Decide decide) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(rentalId)
                .orElseThrow(() -> new RentalRequestException(RentalRequestErrorCode.RENTAL_REQUEST_NOT_FOUND));

        if (!rentalRequest.getPost().getMember().getId().equals(memberId)) {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_REQUEST_OWNER);
        }

        if (rentalRequest.getRequestStatus() != RentalRequestStatus.PENDING) {
            throw new RentalRequestException(RentalRequestErrorCode.ALREADY_PROCESSED);
        }

        if (decide == Decide.APPROVE) {
            rentalRequest.approve();
            rentalRequest.getPost().markAsRented();
            rejectOtherPendingRequests(rentalRequest);
        } else {
            rentalRequest.reject();
        }
        return RentalRequestConverter.toDecisionResult(rentalRequest);
    }

    public RentalRequestResponseDTO.DecisionResult completeReturn(Long memberId, Long rentalRequestId) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() -> new RentalRequestException(RentalRequestErrorCode.RENTAL_REQUEST_NOT_FOUND));

        if (rentalRequest.getRequestStatus() != RentalRequestStatus.APPROVED) {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_RENTING);
        }

        if (rentalRequest.getPost().getMember().getId().equals(memberId)) {
            // 빌린 사람이 반납 완료한 경우
            rentalRequest.completeBorrowerReturn();
        } else if (rentalRequest.getMember().getId().equals(memberId)) {
            // 빌려준 사람이 반납 완료한 경우
            rentalRequest.completeOwnerReturn();
        } else {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_RENTING);
        }
        rentalRequest.getPost().reopen();
        return RentalRequestConverter.toDecisionResult(rentalRequest);
    }

    public RentalRequestResponseDTO.CreatedReview createReview(Long memberId, Long rentalRequestId, RentalRequestRequestDTO.Review request){
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        RentalRequest rentalRequest = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() -> new RentalRequestException(RentalRequestErrorCode.RENTAL_REQUEST_NOT_FOUND));

        if (rentalRequest.getRequestStatus().equals(RentalRequestStatus.COMPLETED)){
            throw new RentalRequestException(RentalRequestErrorCode.REVIEW_NOT_ALLOWED_FOR_UNAPPROVED_RENTAL);
        }

        Member receiver;
        if (writer.getId().equals(rentalRequest.getPost().getMember().getId())) {
            // 내가 빌리는 사람
            receiver = rentalRequest.getMember();
        } else if (writer.getId().equals(rentalRequest.getMember().getId())) {
            // 내가 빌려준 사람
            receiver = rentalRequest.getPost().getMember();
        } else {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_RENTAL_PARTICIPANT);
        }

        Review review = RentalRequestConverter.toReview(writer, receiver, rentalRequest, request);
        Review save = reviewRepository.save(review);

        if (request.reviewSentiment()== ReviewSentiment.GOOD){
            eventPublisher.publishEvent(
                    new MemberRequestDTO.PointGrantEvent(receiver.getId(), PointReason.GOOD_REVIEW)
            );
        } else if (request.reviewSentiment()== ReviewSentiment.BAD){
            eventPublisher.publishEvent(
                    new MemberRequestDTO.PointGrantEvent(receiver.getId(), PointReason.BAD_REVIEW)
            );
        } else {
            throw new MemberException(MemberErrorCode.POINT_INVALID_REQUEST);
        }
        return RentalRequestConverter.toCreatedReview(save);
    }

    private void rejectOtherPendingRequests(RentalRequest approvedRequest) {
        rentalRequestRepository
                .findByPostIdAndRequestStatus(approvedRequest.getPost().getId(), RentalRequestStatus.PENDING)
                .stream()
                .filter(other -> !other.getId().equals(approvedRequest.getId()))
                .forEach(RentalRequest::reject);
    }

    private void validateRentalRequest(Post post, Member member){
        // 자신의 게시글에는 대여 요청 불가
        if (post.getMember().getId().equals(member.getId())) {
            throw new RentalRequestException(
                    RentalRequestErrorCode.CANNOT_REQUEST_OWN_POST
            );
        }

        // 이미 대여 요청한 경우
        if (rentalRequestRepository.existsByPostAndMember(post, member)) {
            throw new RentalRequestException(
                    RentalRequestErrorCode.RENTAL_REQUEST_ALREADY_EXISTS
            );
        }
    }
}
