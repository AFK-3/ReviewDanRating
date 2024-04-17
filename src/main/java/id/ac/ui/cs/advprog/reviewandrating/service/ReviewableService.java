package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;

public interface ReviewableService {
    public Reviewable getReviewable(String listingId);
    public double getAverageRating(String listingId);
}
