package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;

public interface ReviewAndRatingService {
    public ReviewAndRating create(String listingId, String username, String review, int rating);
    public ReviewAndRating findById(String listingId);
    public ReviewAndRating update(String listingId, String username, ReviewAndRating modifiedReviewAndRating);
    public ReviewAndRating delete(String listingId, String username);
    public void allowUserToReview(String username, String listingId);
}
