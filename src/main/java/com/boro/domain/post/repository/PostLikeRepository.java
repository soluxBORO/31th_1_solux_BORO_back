package com.boro.domain.post.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("""
    SELECT DISTINCT pl
    FROM PostLike pl
    JOIN FETCH pl.post p
    JOIN FETCH p.member
    JOIN FETCH p.item
    LEFT JOIN FETCH p.postLikeList
    WHERE pl.member.id = :memberId
    ORDER BY pl.createdAt DESC
    """)
    List<PostLike> findLikedPostsWithLikeCount(
            @Param("memberId") Long memberId
    );

    Optional<PostLike> findByPostAndMember(Post post, Member member);

    long countByPost(Post post);

    boolean existsByPost_IdAndMember_Id(Long postId, Long memberId);

    @Query("""
            SELECT pl.post.id AS postId, COUNT(pl) AS likeCount
            FROM PostLike pl
            WHERE pl.post.id IN :postIds
            GROUP BY pl.post.id
            """)
    List<PostLikeCountView> countGroupByPostIds(@Param("postIds") List<Long> postIds);

    @Query("""
            SELECT pl.post.id
            FROM PostLike pl
            WHERE pl.post.id IN :postIds AND pl.member.id = :memberId
            """)
    List<Long> findLikedPostIds(@Param("postIds") List<Long> postIds, @Param("memberId") Long memberId);

    interface PostLikeCountView {
        Long getPostId();
        Long getLikeCount();
    }
}
