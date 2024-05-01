package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;

import java.util.List;

public interface ReviewByListingService {
    public List<ReviewAndRating> deleteAllReviewInListing(String listingId, String token);
    public Double getAverageRating(String listingId, String token);
    public List<ReviewAndRating> getReviewByListing(String listingId, String token);
}
