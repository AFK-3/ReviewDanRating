package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class ReviewServiceImpl implements  ReviewService{
    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ReviewBuilder reviewBuilder;
    @Async
    public CompletableFuture<Review> create(String listingId, String username, String description, int rating) throws Exception{
        CompletableFuture<Review> futureReview = find(listingId, username);
        Review review = futureReview.get();

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
        return CompletableFuture.completedFuture(review);
    }

    @Async
    public CompletableFuture<Review> find(String listingId, String username) {
        ReviewId reviewId = new ReviewId();
        reviewId.setListingId(listingId);
        reviewId.setUsername(username);

        Optional<Review> optReview = reviewRepo.findById(reviewId);
        return CompletableFuture.completedFuture(optReview.orElse(null));
    }

    @Async
    public CompletableFuture<Review> update(String listingId, String username, Review modifiedReview) throws Exception {
        CompletableFuture<Review> futureReview = find(listingId, username);
        Review review = futureReview.get();

        if (review == null || review.getDescription() == null) {
            throw new IllegalArgumentException(String.format("%s didn't have review on this listing", username));
        }
        modifiedReview = reviewBuilder.setInstance(modifiedReview)
                .addId(listingId, username).build();
        reviewRepo.save(modifiedReview);
        return CompletableFuture.completedFuture(modifiedReview);
    }

    @Async
    public CompletableFuture<Review> delete(String listingId, String username) throws Exception{
        CompletableFuture<Review> futureReview = find(listingId, username);
        Review review = futureReview.get();

        if (review == null || review.getDescription() == null) {
            throw new IllegalArgumentException(String.format("%s didn't have review on this listing", username));
        }
        Review newReview = reviewBuilder.reset()
                .addId(listingId, username)
                .build();
        reviewRepo.delete(review);
        reviewRepo.save(newReview);
        return CompletableFuture.completedFuture(review);
    }

    @Async
    public void allowToReview(String listingId, String username) throws Exception{
        CompletableFuture<Review> futureReview = find(listingId, username);
        Review review = futureReview.get();

        if (review != null) return;

        review = reviewBuilder.reset()
                .addId(listingId, username)
                .build();
        reviewRepo.save(review);
    }
}
