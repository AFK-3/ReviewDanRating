package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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


    public void createReview() {
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
    public void createFakeReview() {
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

    public void createIncompleteReview() {
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
    public void testFindFound() {
        createReview();
        Review foundReview = reviewService.find(reviewId.getListingId(), reviewId.getUsername());
        assertEquals(foundReview, review);
    }

    @Test
    public void testFindNotFound() {
        createFakeReview();
        Review foundReview = reviewService.find(fakeId.getListingId(), fakeId.getUsername());
        assertNull(foundReview);
    }

    @Test
    public void testCreate() {
        createIncompleteReview();
        try {
            when(reviewRepo.save(any(Review.class))).thenReturn(any(Review.class));
            Review createdReview = reviewService.create(incompleteReview.getListingId(), incompleteReview.getUsername(),
                    "Bagus", 8);

            assertEquals(createdReview.getListingId(), incompleteReview.getListingId());
            assertEquals(createdReview.getUsername(), incompleteReview.getUsername());
            assertEquals(createdReview.getDescription(), "Bagus");
            assertEquals(createdReview.getRating(), 8);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreateAlreadyExist() {
        createReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.create(review.getListingId(),
                review.getUsername(),
                review.getDescription(), review.getRating()));
        assertEquals(e.getMessage(), String.format("%s already reviewed this listing", review.getUsername()));
    }

    @Test
    public void testCreateNeverBuy() {
        createFakeReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.create(fakeId.getListingId(),
                fakeId.getUsername(),
                "Sangat Bagus", 10));
        assertEquals(e.getMessage(), String.format("%s never buy this listing", fakeId.getUsername()));
    }

    @Test
    public void testUpdateReview() {
        createReview();
        ReviewBuilder tempBuilder = new ReviewBuilder();
        Review modifiedReview = reviewBuilder.reset().addDescription("Jelek").addRating(2).build();

        try {
            Review updatedReview = reviewService.update(review.getListingId(), review.getUsername(),
                    modifiedReview);

            assertEquals(modifiedReview, updatedReview);
            assertEquals(modifiedReview.getListingId(), review.getListingId());
            assertEquals(modifiedReview.getUsername(), review.getUsername());
            assertEquals(modifiedReview.getDescription(), "Jelek");
            assertEquals(modifiedReview.getRating(), 2);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateButNeverReview() {
        createFakeReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                reviewService.update(fakeId.getListingId(), fakeId.getUsername(), new Review()));

        assertEquals(e.getMessage(), String.format("%s didn't have review on this listing", fakeId.getUsername()));
    }

    @Test
    public void testDeleteReview() {
        createReview();
        try {
            Review deletedReview = reviewService.delete(reviewId.getListingId(), reviewId.getUsername());
            assertEquals(deletedReview, review);

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
    public void testDeleteButNeverReview(){
        createFakeReview();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                reviewService.delete(fakeId.getListingId(), fakeId.getUsername()));

        assertEquals(e.getMessage(), String.format("%s didn't have review on this listing", fakeId.getUsername()));
    }

    @Test
    public void testAllowToReview() {
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
    public void testALlowToReviewAlreadyAllowed() {
        createIncompleteReview();
        reviewService.allowToReview(incompleteReviewId.getListingId(), incompleteReviewId.getUsername());

        verify(reviewRepo, never()).save(any(Review.class));
    }
}
