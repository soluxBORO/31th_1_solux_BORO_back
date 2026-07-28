package com.boro.domain.post.entity;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Item item;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmptySpot emptySpot;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikeList = new ArrayList<>();

    public void updatePostCategory(PostCategory postCategory){
        this.postCategory = postCategory;
    }

    public void assignItem(Item item) {
        this.item = item;
        item.setPost(this);
    }

    public void assignEmptySpot(EmptySpot emptySpot) {
        this.emptySpot = emptySpot;
        emptySpot.setPost(this);
    }

    public void markAsDeleted() {
        this.status = PostStatus.DELETED;
    }

    public void markAsPending() {
        this.status = PostStatus.PENDING;
    }

    public void markAsRented() {
        this.status = PostStatus.RENTED;
    }

    public void reopen() {
        this.status = PostStatus.ACTIVE;
    }

    public void addPostLike(PostLike postLike){
        postLikeList.add(postLike);
        postLike.setPost(this);
    }
}
