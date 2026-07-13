package com.boro.domain.rentalrequest.service.query;

import com.boro.domain.rentalrequest.converter.RentalRequestConverter;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.repository.RentalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentalRequestQueryService {

    private final RentalRequestRepository rentalRequestRepository;

    public List<RentalRequestResponseDTO.RentalRequestPreview> getBorrowedList(Long memberId) {
        return rentalRequestRepository.findBorrowedRequests(memberId).stream()
                .map(RentalRequestConverter::toRentalRequestPreview)
                .toList();
    }

    public List<RentalRequestResponseDTO.RentalRequestPreview> getLentList(Long memberId) {
        return rentalRequestRepository.findLentRequests(memberId).stream()
                .map(RentalRequestConverter::toRentalRequestPreview)
                .toList();
    }
}
