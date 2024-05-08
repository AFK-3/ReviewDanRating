package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;

import java.util.concurrent.CompletableFuture;

public interface ReviewService {
    public CompletableFuture<Review> create(String listingId, String username, String description, int rating) throws Exception;
    public CompletableFuture<Review> find(String listingId, String username);
    public CompletableFuture<Review> update(String listingId, String username, Review modifiedReview) throws Exception;
    public CompletableFuture<Review> delete(String listingId, String username) throws Exception;
    public void allowToReview(String listingId, String username) throws Exception;
}
