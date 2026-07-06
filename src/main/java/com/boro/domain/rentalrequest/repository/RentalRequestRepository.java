package com.boro.domain.rentalrequest.repository;

import com.boro.domain.rentalrequest.entity.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    @Query("""
            SELECT rr FROM RentalRequest rr
            JOIN FETCH rr.post p
            JOIN FETCH p.item i
            JOIN FETCH p.member lender
            WHERE rr.member.id = :memberId
            ORDER BY rr.createdAt DESC
            """)
    List<RentalRequest> findBorrowedRequests(@Param("memberId") Long memberId);
}
