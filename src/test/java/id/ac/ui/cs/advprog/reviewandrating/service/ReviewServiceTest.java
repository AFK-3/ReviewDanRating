package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Spy
    ReviewBuilder reviewBuilder;
    @Mock
    ReviewRepository reviewRepo;
    @InjectMocks
    ReviewServiceImpl reviewService;

    Review review;
    ReviewId reviewId;

    Review incompleteReview;
    ReviewId incompleteReviewId;

    ReviewId fakeId;


    void createReview() {
        ReviewBuilder tempBuilder = new ReviewBuilder();

        String listingId = UUID.randomUUID().toString();
        reviewId = new ReviewId();
        reviewId.setListingId(listingId);
        reviewId.setUsername("Hanau");
        review = tempBuilder.addId(reviewId).addDescription("Sangat Bagus").addRating(10).build();
        when(reviewRepo.findById(ArgumentMatchers.argThat(id ->
            id != null &&
            id.getUsername().equals(reviewId.getUsername()) &&
            id.getListingId().equals(reviewId.getListingId())
        ))).thenReturn(Optional.of(review));
    }
    void createFakeReview() {
        String listingId = UUID.randomUUID().toString();
        fakeId = new ReviewId();
        fakeId.setUsername("EfEmEitch");
        fakeId.setListingId(listingId);
        when(reviewRepo.findById(ArgumentMatchers.argThat(id ->
                id != null &&
                id.getUsername().equals(fakeId.getUsername()) &&
                id.getListingId().equals(fakeId.getListingId())
        ))).thenReturn(Optional.empty());
    }

    void createIncompleteReview() {
        ReviewBuilder tempBuilder = new ReviewBuilder();

        String listingId = UUID.randomUUID().toString();
        incompleteReviewId = new ReviewId();
        incompleteReviewId.setListingId(listingId);
        incompleteReviewId.setUsername("Hanau");
        incompleteReview = tempBuilder.addId(incompleteReviewId).addRating(0).build();
        when(reviewRepo.findById(ArgumentMatchers.argThat(id ->
                id != null &&
                id.getUsername().equals(incompleteReviewId.getUsername()) &&
                id.getListingId().equals(incompleteReviewId.getListingId())
        ))).thenReturn(Optional.of(incompleteReview));
    }

    @Test
    void testFindFound() {
        createReview();
        Review foundReview = reviewService.find(reviewId.getListingId(), reviewId.getUsername());
        assertEquals(review, foundReview);
    }

    @Test
    void testFindNotFound() {
        createFakeReview();
        Review foundReview = reviewService.find(fakeId.getListingId(), fakeId.getUsername());
        assertNull(foundReview);
    }

    @Test
    void testCreate() {
        createIncompleteReview();
        try {
            when(reviewRepo.save(any(Review.class))).thenReturn(any(Review.class));
            Review createdReview = reviewService.create(incompleteReview.getListingId(), incompleteReview.getUsername(),
                    "Bagus", 8);

            assertEquals(incompleteReview.getListingId(), createdReview.getListingId());
            assertEquals(incompleteReview.getUsername(), createdReview.getUsername());
            assertEquals("Bagus", createdReview.getDescription());
            assertEquals(8, createdReview.getRating());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void testCreateAlreadyExist() {
        createReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.create(review.getListingId(),
                review.getUsername(),
                review.getDescription(), review.getRating()));
        assertEquals(String.format("%s already reviewed this listing", review.getUsername()), e.getMessage());
    }

    @Test
    void testCreateNeverBuy() {
        createFakeReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.create(fakeId.getListingId(),
                fakeId.getUsername(),
                "Sangat Bagus", 10));
        assertEquals(String.format("%s never buy this listing", fakeId.getUsername()), e.getMessage());
    }

    @Test
    void testUpdateReview() {
        createReview();
        ReviewBuilder tempBuilder = new ReviewBuilder();
        Review modifiedReview = reviewBuilder.reset().addDescription("Jelek").addRating(2).build();

        try {
            Review updatedReview = reviewService.update(review.getListingId(), review.getUsername(),
                    modifiedReview);

            assertEquals(updatedReview, modifiedReview);
            assertEquals(review.getListingId(), modifiedReview.getListingId());
            assertEquals(review.getUsername(), modifiedReview.getUsername());
            assertEquals("Jelek", modifiedReview.getDescription());
            assertEquals(2, modifiedReview.getRating());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void testUpdateButNeverReview() {
        createIncompleteReview();
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                reviewService.update(incompleteReviewId.getListingId(), incompleteReviewId.getUsername(), new Review()));

        assertEquals(String.format("%s didn't have review on this listing", incompleteReviewId.getUsername()), e1.getMessage());

        createFakeReview();
        String fakeListingId = fakeId.getListingId();
        String fakeUsername = fakeId.getUsername();
        Review fakeNewReview = new Review();
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                reviewService.update(fakeListingId, fakeUsername, fakeNewReview)
        );

        assertEquals(String.format("%s didn't have review on this listing", fakeId.getUsername()), e2.getMessage());
    }

    @Test
    void testDeleteReview() {
        createReview();
        try {
            Review deletedReview = reviewService.delete(reviewId.getListingId(), reviewId.getUsername());
            assertEquals(review, deletedReview);

            verify(reviewRepo).save(ArgumentMatchers.argThat(id ->
                    id != null &&
                    id.getListingId().equals(review.getListingId()) &&
                    id.getUsername().equals(review.getUsername()) &&
                    id.getDescription() == null &&
                    id.getRating() == 0));
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void testDeleteButNeverReview(){
        createIncompleteReview();
        String incompleteListingId = incompleteReviewId.getListingId();
        String incompleteUsername = incompleteReviewId.getUsername();
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                reviewService.delete(incompleteListingId, incompleteUsername)
        );

        assertEquals(String.format("%s didn't have review on this listing", incompleteReviewId.getUsername()), e1.getMessage());

        createFakeReview();
        String fakeListingId = fakeId.getListingId();
        String fakeUsername = fakeId.getUsername();
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                reviewService.delete(fakeListingId, fakeUsername)
        );

        assertEquals(String.format("%s didn't have review on this listing", fakeId.getUsername()), e2.getMessage());
    }

    @Test
    void testAllowToReview() {
        createFakeReview();
        reviewService.allowToReview(fakeId.getListingId(), fakeId.getUsername());
        verify(reviewRepo).save(ArgumentMatchers.argThat(id ->
                id != null &&
                id.getUsername().equals(fakeId.getUsername()) &&
                id.getListingId().equals(fakeId.getListingId()) &&
                id.getRating() == 0 &&
                id.getDescription() == null));
    }

    @Test
    void testALlowToReviewAlreadyAllowed() {
        createIncompleteReview();
        reviewService.allowToReview(incompleteReviewId.getListingId(), incompleteReviewId.getUsername());

        verify(reviewRepo, never()).save(any(Review.class));
    }
}
