package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements  ReviewService{
    private final ReviewRepository reviewRepo;

    private final ReviewBuilder reviewBuilder;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo, ReviewBuilder reviewBuilder) {
        this.reviewRepo = reviewRepo;
        this.reviewBuilder = reviewBuilder;
    }

    public Review create(String listingId, String username, String description, int rating) throws IllegalArgumentException{
        Review review = find(listingId, username);

        if (review == null) {
            throw new IllegalArgumentException(String.format("%s never buy this listing", username));
        }
        else if (review.getDescription() != null) {
            throw new IllegalArgumentException(String.format("%s already reviewed this listing", username));
        }

        review = reviewBuilder.reset()
                .addId(listingId, username)
                .addDescription(description)
                .addRating(rating)
                .build();

        reviewRepo.save(review);
        return review;
    }

    public Review find(String listingId, String username) {
        ReviewId reviewId = new ReviewId();
        reviewId.setListingId(listingId);
        reviewId.setUsername(username);

        Optional<Review> optReview = reviewRepo.findById(reviewId);
        return optReview.orElse(null);
    }

    public Review update(String listingId, String username, Review modifiedReview) throws IllegalArgumentException {
        Review review = find(listingId, username);

        if (review == null || review.getDescription() == null) {
            throw new IllegalArgumentException(String.format("%s didn't have review on this listing", username));
        }
        modifiedReview = reviewBuilder.setInstance(modifiedReview)
                .addId(listingId, username).build();
        reviewRepo.save(modifiedReview);
        return modifiedReview;
    }

    public Review delete(String listingId, String username) throws IllegalArgumentException{
        Review review = find(listingId, username);

        if (review == null || review.getDescription() == null) {
            throw new IllegalArgumentException(String.format("%s didn't have review on this listing", username));
        }
        Review newReview = reviewBuilder.reset()
                .addId(listingId, username)
                .build();
        reviewRepo.delete(review);
        reviewRepo.save(newReview);
        return review;
    }

    public void allowToReview(String listingId, String username){
        Review review = find(listingId, username);

        if (review != null) return;

        review = reviewBuilder.reset()
                .addId(listingId, username)
                .build();
        reviewRepo.save(review);
    }
}
