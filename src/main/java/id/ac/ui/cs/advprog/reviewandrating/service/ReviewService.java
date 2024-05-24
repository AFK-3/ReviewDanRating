package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

public interface ReviewService {
    public Review create(String listingId, String username, String description, int rating) throws IllegalArgumentException;
    public Review find(String listingId, String username);
    public Review update(String listingId, String username, Review modifiedReview) throws IllegalArgumentException;
    public Review delete(String listingId, String username) throws IllegalArgumentException;
    public void allowToReview(String listingId, String username) throws IllegalArgumentException;
}
