package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

import java.util.List;

public interface ReviewPerListingService {
    public List<Review> getReviews(String listingId);
    public void deleteReviewInListing(String listingId);
    public double averageRating(String listingId);
    public boolean isListingExist(String listingId, String token);
}
