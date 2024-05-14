package id.ac.ui.cs.advprog.reviewandrating.Repository;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
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
        assertEquals(reviewList.size(), 1);

        Review storedReview = reviewList.getFirst();
        assertEquals(storedReview.getUsername(), "Farrell");
        assertEquals(storedReview.getListingId(), defaultListingId);
        assertEquals(storedReview.getRating(), 5);
        assertEquals(storedReview.getDescription(), "Mid");
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
        assertEquals(reviewList.size(), 1);

        Review storedReview = reviewList.getFirst();
        assertEquals(storedReview.getUsername(), review.getUsername());
        assertEquals(storedReview.getListingId(), review.getListingId());
        assertEquals(storedReview.getRating(), review.getRating());
        assertEquals(storedReview.getDescription(), review.getDescription());

    }

    @Test
    public void testFindByListingId() {
        List<Review> reviewList = reviewRepository.findByListingId(defaultListingId).stream().toList();
        assertEquals(reviewList.size(), 1);

        Review storedReview = reviewList.getFirst();
        assertEquals(storedReview.getUsername(), "Farrell");
        assertEquals(storedReview.getListingId(), defaultListingId);
        assertEquals(storedReview.getRating(), 5);
        assertEquals(storedReview.getDescription(), "Mid");

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
        assertEquals(optReview.isPresent(), false);

        reviewId.setUsername("EfEmEitch");
        optReview = reviewRepository.findById(reviewId);
        assertEquals(optReview.isPresent(), false);
    }
}