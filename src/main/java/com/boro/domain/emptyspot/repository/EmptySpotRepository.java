package com.boro.domain.emptyspot.repository;

import com.boro.domain.post.entity.EmptySpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EmptySpotRepository extends JpaRepository<EmptySpot, Long> {

    @Query("""
            SELECT es FROM EmptySpot es
            JOIN FETCH es.post p
            JOIN FETCH p.member m
            WHERE p.status = com.boro.domain.post.entity.enums.PostStatus.ACTIVE
              AND es.expectedCheckoutTime > :now
              AND m.active = true
            ORDER BY es.expectedCheckoutTime ASC
            """)
    List<EmptySpot> findValidEmptySpots(@Param("now") LocalDateTime now);
}
