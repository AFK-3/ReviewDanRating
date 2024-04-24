package id.ac.ui.cs.advprog.reviewandrating.model.builder;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReviewableBuilder {
    private Reviewable reviewable;

    public ReviewableBuilder() {
        reviewable = new Reviewable();
    }

    public ReviewableBuilder reset() {
        reviewable = new Reviewable();
        return this;
    }

    public ReviewableBuilder addListingId(String listingId){
        reviewable.setListingId(listingId);
        return this;
    }

    public ReviewableBuilder addReviews(Map<String, ReviewAndRating> reviews){
        reviewable.setReviews(reviews);
        return this;
    }

    public Reviewable build() {
        return this.reviewable;
    }
}
