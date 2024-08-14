package com.khair.library_app_backend.service;

import com.khair.library_app_backend.dao.ReviewRepository;
import com.khair.library_app_backend.entity.Review;
import com.khair.library_app_backend.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());
        if (validReview != null) {
            throw new Exception("Review already exists");
        }

        Review review = new Review();
        review.setUserEmail(userEmail);
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        if (reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    Object::toString
            ).orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateReview != null) {
            return true;
        } else {
            return false;
        }
    }

}
