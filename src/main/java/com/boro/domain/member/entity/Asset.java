package com.boro.domain.member.entity;

import com.boro.domain.member.entity.enums.AssetCategory;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "asset")
public class Asset extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AssetCategory assetCategory;

    private Integer price;

}
