package com.boro.domain.rentalrequest.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    @Query("""
            SELECT rr FROM RentalRequest rr
            JOIN FETCH rr.post p
            JOIN FETCH p.item i
            JOIN FETCH p.member borrower
            WHERE rr.member.id = :memberId
              AND rr.requestStatus IN (
            com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.PENDING,
            com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.APPROVED
            )
            ORDER BY rr.createdAt DESC
            """)
    List<RentalRequest> findBorrowedRequests(@Param("memberId") Long memberId);

    @Query("""
            SELECT rr FROM RentalRequest rr
            JOIN FETCH rr.post p
            JOIN FETCH p.item i
            JOIN FETCH rr.member lender
            WHERE p.member.id = :memberId
            AND rr.requestStatus = com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.APPROVED
            ORDER BY rr.createdAt DESC
            """)
    List<RentalRequest> findLentRequests(@Param("memberId") Long memberId);

    List<RentalRequest> findByPostIdAndRequestStatus(Long postId, RentalRequestStatus requestStatus);

    boolean existsByPostAndMember(Post post, Member member);
}
