package com.boro.domain.rentalrequest.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    @Query("""
            SELECT rr FROM RentalRequest rr
            JOIN FETCH rr.post p
            JOIN FETCH rr.chatRoom cr
            LEFT JOIN FETCH p.item i
            JOIN FETCH p.member postAuthor
            JOIN FETCH rr.member requester
            WHERE ((
                p.postCategory <>
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                AND postAuthor.id = :memberId
            ) OR (
                p.postCategory =
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                AND requester.id = :memberId
            ))
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
            JOIN FETCH rr.chatRoom cr
            LEFT JOIN FETCH p.item i
            JOIN FETCH p.member postAuthor
            JOIN FETCH rr.member requester
            WHERE ((
                p.postCategory <>
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                AND requester.id = :memberId
                AND rr.requestStatus = com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.APPROVED
            ) OR (
                p.postCategory =
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                AND postAuthor.id = :memberId
                AND rr.requestStatus IN (
                    com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.PENDING,
                    com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.APPROVED
                )
            ))
            ORDER BY rr.createdAt DESC
            """)
    List<RentalRequest> findLentRequests(@Param("memberId") Long memberId);

    List<RentalRequest> findByPostIdAndRequestStatus(Long postId, RentalRequestStatus requestStatus);

    boolean existsByPostAndMember(Post post, Member member);

    @Query("""
        SELECT rr.post FROM RentalRequest rr
        JOIN rr.post p
        WHERE rr.requestStatus =
                  com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.COMPLETED 
        AND (p.member.id = :memberId
        OR rr.member.id = :memberId)
        ORDER BY rr.updatedAt DESC
    """)
    List<Post> findCompletedPostsByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT rr.post FROM RentalRequest rr
        JOIN rr.post p
        WHERE rr.requestStatus =
                  com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus.COMPLETED
          AND ((
                p.postCategory =
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                and p.member.id = :memberId
            ) or (
                p.postCategory <>
                    com.boro.domain.post.entity.enums.PostCategory.EMPTY_SPOTS
                and rr.member.id = :memberId
            ))
        ORDER BY rr.updatedAt DESC
    """)
    List<Post> findProvidedPostsByMemberId(@Param("memberId") Long memberId);

    Optional<RentalRequest> findByMemberAndPost(Member member, Post post);

}
