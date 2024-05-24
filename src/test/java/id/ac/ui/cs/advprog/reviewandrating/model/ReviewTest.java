package id.ac.ui.cs.advprog.reviewandrating.model;

import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals("Hanau", review.getUsername());
        assertEquals(listingId, review.getListingId());
        assertEquals(10, review.getRating());
        assertEquals("Sangat Bagus", review.getDescription());
    }

    @Test
    public void testReviewIdGetterSetter() {
        ReviewId reviewId = new ReviewId();
        String listingId = UUID.randomUUID().toString();

        reviewId.setUsername("Hanau");
        reviewId.setListingId(listingId);

        assertEquals("Hanau", reviewId.getUsername());
        assertEquals(listingId, reviewId.getListingId());
    }

    @Test
    public void testReviewBuilder() {
        String listingId = UUID.randomUUID().toString();
        Review review = reviewBuilder.addId(listingId, "Hanau")
                .addRating(10)
                .addDescription("Sangat Bagus")
                .build();

        assertEquals("Hanau", review.getUsername());
        assertEquals(listingId, review.getListingId());
        assertEquals(10, review.getRating());
        assertEquals( "Sangat Bagus", review.getDescription());
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

        assertEquals("Hanau", review.getUsername());
        assertEquals(listingId, review.getListingId());
        assertEquals(10, review.getRating());
        assertEquals("Sangat Bagus", review.getDescription());
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

        assertEquals(listingId, review.getListingId());
        assertEquals("Farrell", review.getUsername());
        assertEquals(5, review.getRating());
        assertEquals("Mid", review.getDescription());
    }
}
