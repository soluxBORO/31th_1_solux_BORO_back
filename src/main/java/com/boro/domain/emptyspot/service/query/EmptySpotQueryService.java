package com.boro.domain.emptyspot.service.query;

import com.boro.domain.emptyspot.converter.EmptySpotConverter;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.emptyspot.repository.EmptySpotRepository;
import com.boro.domain.post.entity.EmptySpot;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.global.error.code.status.EmptySpotErrorCode;
import com.boro.global.error.exception.handler.EmptySpotException;
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

    public EmptySpotResponseDTO.EmptySpotInfo getEmptySpotDetail(Long emptySpotId) {
        EmptySpot emptySpot = emptySpotRepository.findById(emptySpotId)
                .orElseThrow(() -> new EmptySpotException(EmptySpotErrorCode.EMPTY_SPOT_NOT_FOUND));

        if (emptySpot.getPost().getStatus() == PostStatus.DELETED) {
            throw new EmptySpotException(EmptySpotErrorCode.EMPTY_SPOT_NOT_FOUND);
        }

        return EmptySpotConverter.toEmptySpotInfo(emptySpot);
    }
}
