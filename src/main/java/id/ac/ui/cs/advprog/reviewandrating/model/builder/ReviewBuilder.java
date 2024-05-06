package id.ac.ui.cs.advprog.reviewandrating.model.builder;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import org.springframework.stereotype.Component;

@Component
public class ReviewBuilder {
    private Review review;

    public ReviewBuilder() {
        this.review = new Review();
    }

    public ReviewBuilder reset() {
        this.review = new Review();
        return this;
    }

    public ReviewBuilder setInstance(Review review) {
        this.review = review;
        return this;
    }

    public ReviewBuilder addId(ReviewId reviewId) {
        this.review.setListingId(reviewId.getListingId());
        this.review.setUsername(reviewId.getUsername());
        return this;
    }

    public ReviewBuilder addId(String listingId, String username) {
        this.review.setListingId(listingId);
        this.review.setUsername(username);
        return this;
    }

    public ReviewBuilder addDescription(String description) {
        this.review.setDescription(description);
        return this;
    }

    public ReviewBuilder addRating(int rating) {
        this.review.setRating(rating);
        return this;
    }

    public Review build() {
        return this.review;
    }
}
