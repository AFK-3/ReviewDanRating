package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

public interface ReviewService {
    public Review create(String listingId, String username, String description, int rating);
    public Review find(String listingId, String username);
    public Review update(String listingId, String username, Review modifiedReview);
    public Review delete(String listingId, String username);
    public void allowToReview(String listingId, String username);
}
