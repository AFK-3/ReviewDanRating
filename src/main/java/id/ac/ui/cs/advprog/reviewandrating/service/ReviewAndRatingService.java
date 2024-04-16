package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;

public interface ReviewAndRatingService {
    public ReviewAndRating create(String listingId, String username, String review, int rating);
    public ReviewAndRating findById(String listingId);
    public ReviewAndRating update(String listingId, ReviewAndRating modifiedReviewAndRating);
    public ReviewAndRating delete(String id, String listingId);
}
