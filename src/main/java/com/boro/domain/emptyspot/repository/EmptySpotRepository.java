package com.boro.domain.emptyspot.repository;

import com.boro.domain.post.entity.EmptySpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmptySpotRepository extends JpaRepository<EmptySpot, Long> {
}
