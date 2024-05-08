package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReviewPerListingService {
    public CompletableFuture<List<Review>> getReviews(String listingId);
    public void deleteReviewInListing(String listingId);
    public CompletableFuture<Double> averageRating(String listingId);
    public CompletableFuture<Boolean> isListingExist(String listingId, String token);
}
