package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class ReviewPerListingServiceImpl implements ReviewPerListingService{
    private final ReviewRepository reviewRepo;

    @Autowired
    public ReviewPerListingServiceImpl(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public List<Review> getReviews(String listingId) {
        return reviewRepo.findByListingId(listingId);
    }
    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> deleteReviewInListing(String listingId) {
        reviewRepo.deleteByListingId(listingId);
        return CompletableFuture.completedFuture(null);
    }

    public Double averageRating(String listingId) {
        return reviewRepo.findAverageRating(listingId);
    }

}
