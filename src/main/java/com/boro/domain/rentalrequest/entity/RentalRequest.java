package com.boro.domain.rentalrequest.entity;

import com.boro.domain.chat.entity.ChatRoom;
import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
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

    @Builder.Default
    private boolean borrowerReturned = false;

    @Builder.Default
    private boolean ownerReturned = false;

    // 빌려주는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // post.getMember() : 빌리는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToOne(mappedBy = "rentalRequest", fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public void approve() {
        this.requestStatus = RentalRequestStatus.APPROVED;
    }

    public void reject() {
        this.requestStatus = RentalRequestStatus.REJECTED;
    }

    public void completeBorrowerReturn() {
        this.borrowerReturned = true;
        updateCompletedStatus();
    }

    public void completeOwnerReturn() {
        this.ownerReturned = true;
        updateCompletedStatus();
    }

    private void updateCompletedStatus() {
        if (borrowerReturned && ownerReturned) {
            this.requestStatus = RentalRequestStatus.COMPLETED;
            post.markAsCompleted();
        }
    }

    public void assignChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
