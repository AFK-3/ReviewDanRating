package id.ac.ui.cs.advprog.reviewandrating.repository;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ReviewRepoTest {
    @Autowired
    ReviewRepository reviewRepository;
    ReviewBuilder reviewBuilder;
    String defaultListingId;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        reviewBuilder = new ReviewBuilder();
        defaultListingId = UUID.randomUUID().toString();

        Review defaultReview1 = reviewBuilder.reset()
                .addId(defaultListingId, "Farrell")
                .addRating(5)
                .addDescription("Mid")
                .build();
        reviewRepository.save(defaultReview1);

        Review defaultReview2 = reviewBuilder.reset()
                .addId(defaultListingId, "EfEmEitch")
                .addRating(1)
                .build();
        reviewRepository.save(defaultReview2);

        reviewBuilder.reset();
    }

    @Test
    public void testFindById() {
        ReviewId reviewId = new ReviewId();
        reviewId.setUsername("Farrell");
        reviewId.setListingId(defaultListingId);

        List<Review> reviewList = reviewRepository.findById(reviewId).stream().toList();
        assertEquals(1, reviewList.size());

        Review storedReview = reviewList.getFirst();
        assertEquals("Farrell", storedReview.getUsername());
        assertEquals(defaultListingId, storedReview.getListingId());
        assertEquals(5, storedReview.getRating());
        assertEquals("Mid", storedReview.getDescription());
    }

    @Test
    public void testSave() {
        String listingId = UUID.randomUUID().toString();

        Review review = reviewBuilder.addId(listingId, "Hanau")
                .addRating(8)
                .addDescription("Bagus")
                .build();
        reviewRepository.save(review);

        ReviewId reviewId = new ReviewId();
        reviewId.setListingId(listingId);
        reviewId.setUsername("Hanau");

        List<Review> reviewList = reviewRepository.findById(reviewId).stream().toList();
        assertEquals(1, reviewList.size());

        Review storedReview = reviewList.getFirst();
        assertEquals(review.getUsername(), storedReview.getUsername());
        assertEquals(review.getListingId(), storedReview.getListingId());
        assertEquals(review.getRating(), storedReview.getRating());
        assertEquals(review.getDescription(), storedReview.getDescription());

    }

    @Test
    public void testFindByListingId() {
        List<Review> reviewList = reviewRepository.findByListingId(defaultListingId).stream().toList();
        assertEquals(1, reviewList.size());

        Review storedReview = reviewList.getFirst();
        assertEquals("Farrell", storedReview.getUsername());
        assertEquals(defaultListingId, storedReview.getListingId());
        assertEquals(5, storedReview.getRating());
        assertEquals("Mid", storedReview.getDescription());

    }

    @Test
    public void testFindAverageRating() {
        Double rating = reviewRepository.findAverageRating(defaultListingId);
        assertEquals(rating, 5.0);
    }

    @Test
    public void testDeleteByListing() {
        reviewRepository.deleteByListingId(defaultListingId);
        List<Review> listReview = reviewRepository.findByListingId(defaultListingId);
        assertEquals(listReview.size(), 0);

        ReviewId reviewId = new ReviewId();
        reviewId.setListingId(defaultListingId);
        reviewId.setUsername("Farrell");

        entityManager.flush();
        entityManager.clear();

        Optional<Review> optReview = reviewRepository.findById(reviewId);
        assertFalse(optReview.isPresent());

        reviewId.setUsername("EfEmEitch");
        optReview = reviewRepository.findById(reviewId);
        assertFalse(optReview.isPresent());
    }
}