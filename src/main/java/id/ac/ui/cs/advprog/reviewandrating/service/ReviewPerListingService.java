package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReviewPerListingService {
    public List<Review> getReviews(String listingId);
    public CompletableFuture<Void> deleteReviewInListing(String listingId);
    public Double averageRating(String listingId);
}
