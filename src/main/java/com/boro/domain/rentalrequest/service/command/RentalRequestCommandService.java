package com.boro.domain.rentalrequest.service.command;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.converter.RentalRequestConverter;
import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.enums.RentalProgressStatus;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import com.boro.domain.rentalrequest.repository.RentalRequestRepository;
import com.boro.global.error.code.status.RentalRequestErrorCode;
import com.boro.global.error.exception.handler.RentalRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalRequestCommandService {

    private final RentalRequestRepository rentalRequestRepository;

    public RentalRequest createRentalRequest(Post post, Member member){
        validateRentalRequest(post, member);
        RentalRequest rentalRequest = RentalRequestConverter.toRentalRequest(member, post);
        return rentalRequestRepository.save(rentalRequest);
    }

    public void decide(Long memberId, RentalRequestRequestDTO.Decide request) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(request.rentalRequestId())
                .orElseThrow(() -> new RentalRequestException(RentalRequestErrorCode.RENTAL_REQUEST_NOT_FOUND));

        if (!rentalRequest.getPost().getMember().getId().equals(memberId)) {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_REQUEST_OWNER);
        }

        if (rentalRequest.getRequestStatus() != RentalRequestStatus.PENDING) {
            throw new RentalRequestException(RentalRequestErrorCode.ALREADY_PROCESSED);
        }

        if (request.decision() == RentalRequestRequestDTO.Decision.APPROVE) {
            rentalRequest.approve();
            rentalRequest.getPost().markAsRented();
            rejectOtherPendingRequests(rentalRequest);
        } else {
            rentalRequest.reject();
        }
    }

    public void completeReturn(Long memberId, RentalRequestRequestDTO.Complete request) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(request.rentalRequestId())
                .orElseThrow(() -> new RentalRequestException(RentalRequestErrorCode.RENTAL_REQUEST_NOT_FOUND));

        if (!rentalRequest.getPost().getMember().getId().equals(memberId)) {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_REQUEST_OWNER);
        }

        if (rentalRequest.getRequestStatus() != RentalRequestStatus.APPROVED
                || rentalRequest.getProgressStatus() != RentalProgressStatus.RENTING) {
            throw new RentalRequestException(RentalRequestErrorCode.NOT_RENTING);
        }

        rentalRequest.complete();
        rentalRequest.getPost().reopen();
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
