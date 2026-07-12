package com.boro.domain.emptyspot.converter;

import com.boro.domain.emptyspot.dto.request.EmptySpotRequestDTO;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.post.entity.EmptySpot;
import com.boro.domain.post.entity.Post;

public class EmptySpotConverter {

    public static EmptySpot toEmptySpot(EmptySpotRequestDTO.CreateEmptySpot request) {
        return EmptySpot.builder()
                .location(request.location())
                .floor(request.floor())
                .seatNumber(request.seatNumber())
                .hasPowerOutlet(request.hasPowerOutlet())
                .hasWindowSeat(request.hasWindowSeat())
                .expectedCheckoutTime(request.expectedCheckoutTime())
                .build();
    }

    public static EmptySpotResponseDTO.EmptySpotDetail toEmptySpotDetail(Post post) {
        EmptySpot emptySpot = post.getEmptySpot();
        return EmptySpotResponseDTO.EmptySpotDetail.builder()
                .postId(post.getId())
                .location(emptySpot.getLocation())
                .floor(emptySpot.getFloor())
                .seatNumber(emptySpot.getSeatNumber())
                .hasPowerOutlet(emptySpot.getHasPowerOutlet())
                .hasWindowSeat(emptySpot.getHasWindowSeat())
                .expectedCheckoutTime(emptySpot.getExpectedCheckoutTime())
                .build();
    }
}
