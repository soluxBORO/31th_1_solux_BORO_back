package com.boro.domain.post.entity;

import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "empty_spot")
public class EmptySpot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empty_spot_id")
    private Long id;

    private String location;

    private Integer floor;

    private Integer seatNumber;

    private Boolean hasPowerOutlet;

    private Boolean hasWindowSeat;

    private LocalDateTime expectedCheckoutTime;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;
}
