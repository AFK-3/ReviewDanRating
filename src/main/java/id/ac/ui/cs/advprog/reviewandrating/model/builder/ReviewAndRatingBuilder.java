package id.ac.ui.cs.advprog.reviewandrating.model.builder;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReviewAndRatingBuilder {
    private ReviewAndRating reviewAndRating;

    public ReviewAndRatingBuilder() {
        reviewAndRating = new ReviewAndRating();
    }

    public ReviewAndRatingBuilder reset() {
        reviewAndRating = new ReviewAndRating();
        return this;
    }

    public ReviewAndRatingBuilder addId() {
        reviewAndRating.setId(UUID.randomUUID());
        return this;
    }

    public ReviewAndRatingBuilder addWriter(String writerName) {
        reviewAndRating.setWriter(writerName);
        return this;
    }

    public ReviewAndRatingBuilder addReview(String review){
        reviewAndRating.setReview(review);
        return this;
    }

    public ReviewAndRatingBuilder addRating(int rating){
        reviewAndRating.setRating(rating);
        return this;
    }

    public ReviewAndRating build() {
        return this.reviewAndRating;
    }
}
