package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;

import java.util.List;

public interface ReviewByListingService {
    public List<ReviewAndRating> deleteAllReviewInListing(String listingId);
    public Double getAverageRating(String listingId);
    public List<ReviewAndRating> getReviewByListing(String listingId);
}
