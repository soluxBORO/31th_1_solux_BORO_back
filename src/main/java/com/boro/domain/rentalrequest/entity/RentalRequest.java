package com.boro.domain.rentalrequest.entity;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.entity.enums.RentalProgressStatus;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rental_request")
public class RentalRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_request_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RentalRequestStatus requestStatus = RentalRequestStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private RentalProgressStatus progressStatus;

    // 빌려주는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // post.getMember() : 빌리는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void approve() {
        this.requestStatus = RentalRequestStatus.APPROVED;
        this.progressStatus = RentalProgressStatus.RENTING;
    }

    public void reject() {
        this.requestStatus = RentalRequestStatus.REJECTED;
    }

    public void complete() {
        this.progressStatus = RentalProgressStatus.RETURNED;
    }
}
