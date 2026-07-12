package com.boro.domain.emptyspot.service.query;

import com.boro.domain.emptyspot.converter.EmptySpotConverter;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.emptyspot.repository.EmptySpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmptySpotQueryService {

    private final EmptySpotRepository emptySpotRepository;

    public List<EmptySpotResponseDTO.EmptySpotSummary> getEmptySpotList() {
        return emptySpotRepository.findValidEmptySpots(LocalDateTime.now()).stream()
                .map(EmptySpotConverter::toEmptySpotSummary)
                .toList();
    }
}
