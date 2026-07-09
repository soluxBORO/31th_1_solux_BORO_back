package com.boro.domain.rentalrequest.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.rentalrequest.entity.Review;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReceiverAndReviewSentimentOrderByCreatedAtDesc(Member receiver, ReviewSentiment reviewSentiment);

    List<Review> findByWriterAndReviewSentimentOrderByCreatedAtDesc(Member writer, ReviewSentiment reviewSentiment);

    Integer countByReceiverAndReviewSentiment(Member receiver, ReviewSentiment reviewSentiment);
    Integer countByWriterAndReviewSentiment(Member writer, ReviewSentiment reviewSentiment);
}
