package com.boro.domain.post.entity;

import com.boro.domain.post.entity.enums.RentalPriceUnit;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate rentalStartTime;

    private LocalDate rentalEndTime;

    private Integer rentalPrice;

    @Enumerated(EnumType.STRING)
    private RentalPriceUnit rentalPriceUnit;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    public void addImage(ItemImage itemImage) {
        itemImages.add(itemImage);
        itemImage.setItem(this);
    }

    public void updateInfo(String title, String description,
                           LocalDate rentalStartTime, LocalDate rentalEndTime,
                           Integer rentalPrice, RentalPriceUnit rentalPriceUnit) {
        this.title = title;
        this.description = description;
        this.rentalStartTime = rentalStartTime;
        this.rentalEndTime = rentalEndTime;
        this.rentalPrice = rentalPrice;
        this.rentalPriceUnit = rentalPriceUnit;
    }

    public void updateImages(List<String> imageUrlList) {
        itemImages.clear();
        imageUrlList.forEach(imageUrl -> addImage(ItemImage.builder().imageUrl(imageUrl).build()));
    }
}
