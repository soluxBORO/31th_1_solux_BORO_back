package com.boro.domain.rentalrequest.converter;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.enums.RentalProgressStatus;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;

public class RentalRequestConverter {

    public static RentalRequestResponseDTO.BorrowedItem toBorrowedItem(RentalRequest rentalRequest) {
        Item item = rentalRequest.getPost().getItem();
        return RentalRequestResponseDTO.BorrowedItem.builder()
                .requestStatus(toDisplayStatus(rentalRequest))
                .rentalStartTime(item.getRentalStartTime())
                .lender(rentalRequest.getPost().getMember().getNickname())
                .title(item.getTitle())
                .build();
    }

    public static RentalRequestResponseDTO.LentItem toLentItem(RentalRequest rentalRequest) {
        Item item = rentalRequest.getPost().getItem();
        return RentalRequestResponseDTO.LentItem.builder()
                .requestStatus(toDisplayStatus(rentalRequest))
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .borrower(rentalRequest.getMember().getNickname())
                .title(item.getTitle())
                .build();
    }

    private static RentalRequestResponseDTO.DisplayStatus toDisplayStatus(RentalRequest rentalRequest) {
        if (rentalRequest.getRequestStatus() == RentalRequestStatus.REJECTED) {
            return RentalRequestResponseDTO.DisplayStatus.REJECTED;
        }
        if (rentalRequest.getRequestStatus() == RentalRequestStatus.PENDING) {
            return RentalRequestResponseDTO.DisplayStatus.PENDING;
        }
        return rentalRequest.getProgressStatus() == RentalProgressStatus.RETURNED
                ? RentalRequestResponseDTO.DisplayStatus.RETURNED
                : RentalRequestResponseDTO.DisplayStatus.RENTING;
    }

    public static RentalRequest toRentalRequest(Member member, Post post){
        return RentalRequest.builder()
                .member(member)
                .post(post)
                .build();
    }
}
