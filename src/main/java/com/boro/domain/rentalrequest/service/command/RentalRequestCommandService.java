package com.boro.domain.rentalrequest.service.command;

import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
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
            rejectOtherPendingRequests(rentalRequest);
        } else {
            rentalRequest.reject();
        }
    }

    private void rejectOtherPendingRequests(RentalRequest approvedRequest) {
        rentalRequestRepository
                .findByPostIdAndRequestStatus(approvedRequest.getPost().getId(), RentalRequestStatus.PENDING)
                .stream()
                .filter(other -> !other.getId().equals(approvedRequest.getId()))
                .forEach(RentalRequest::reject);
    }
}
