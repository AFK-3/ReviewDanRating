package id.ac.ui.cs.advprog.reviewandrating.model;

import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {
    ReviewBuilder reviewBuilder;

    @BeforeEach
    public void setUp() {
        reviewBuilder = new ReviewBuilder().reset();
    }

    @Test
    public void testReviewGetterSetter() {
        Review review = new Review();
        String listingId = UUID.randomUUID().toString();

        review.setUsername("Hanau");
        review.setListingId(listingId);
        review.setRating(10);
        review.setDescription("Sangat Bagus");

        assertEquals(review.getUsername(), "Hanau");
        assertEquals(review.getListingId(), listingId);
        assertEquals(review.getRating(), 10);
        assertEquals(review.getDescription(), "Sangat Bagus");
    }

    @Test
    public void testReviewIdGetterSetter() {
        ReviewId reviewId = new ReviewId();
        String listingId = UUID.randomUUID().toString();

        reviewId.setUsername("Hanau");
        reviewId.setListingId(listingId);

        assertEquals(reviewId.getUsername(), "Hanau");
        assertEquals(reviewId.getListingId(), listingId);
    }

    @Test
    public void testReviewBuilder() {
        String listingId = UUID.randomUUID().toString();
        Review review = reviewBuilder.addId(listingId, "Hanau")
                .addRating(10)
                .addDescription("Sangat Bagus")
                .build();

        assertEquals(review.getUsername(), "Hanau");
        assertEquals(review.getListingId(), listingId);
        assertEquals(review.getRating(), 10);
        assertEquals(review.getDescription(), "Sangat Bagus");
    }

    @Test
    public void testReviewBuilderWithListingId() {
        ReviewId reviewId = new ReviewId();
        String listingId = UUID.randomUUID().toString();
        reviewId.setUsername("Hanau");
        reviewId.setListingId(listingId);

        Review review = reviewBuilder.addId(reviewId)
                .addRating(10)
                .addDescription("Sangat Bagus")
                .build();

        assertEquals(review.getUsername(), "Hanau");
        assertEquals(review.getListingId(), listingId);
        assertEquals(review.getRating(), 10);
        assertEquals(review.getDescription(), "Sangat Bagus");
    }

    @Test
    public void testBuilderModification() {
        Review review = new Review();
        String listingId = UUID.randomUUID().toString();
        review.setListingId(listingId);
        review.setUsername("Hanau");
        review.setRating(10);
        review.setDescription("Sangat Bagus");

        listingId = UUID.randomUUID().toString();
        reviewBuilder.setInstance(review)
                .addId(listingId, "Farrell")
                .addRating(5)
                .addDescription("Mid");

        assertEquals(review.getListingId(), listingId);
        assertEquals(review.getUsername(), "Farrell");
        assertEquals(review.getRating(), 5);
        assertEquals(review.getDescription(), "Mid");
    }
}
