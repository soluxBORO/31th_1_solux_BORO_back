package com.boro.domain.rentalrequest.repository;

import com.boro.domain.rentalrequest.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
