package com.boro.domain.post.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.EmptySpot;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.item i
            JOIN FETCH p.member m
            WHERE p.status <> com.boro.domain.post.entity.enums.PostStatus.DELETED
              AND (:category IS NULL OR p.postCategory = :category)
              AND (:onlyAvailable = false OR p.status = com.boro.domain.post.entity.enums.PostStatus.ACTIVE)
            ORDER BY p.createdAt DESC
            """)
    List<Post> findItemPostList(@Param("category") PostCategory category, @Param("onlyAvailable") boolean onlyAvailable);

    List<Post> findByMemberOrderByCreatedAtDesc(Member member);
}
