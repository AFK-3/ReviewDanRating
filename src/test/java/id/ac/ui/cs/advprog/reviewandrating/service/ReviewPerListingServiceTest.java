package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

import java.net.http.HttpResponse;
import java.sql.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewPerListingServiceTest {
    @Mock
    ReviewRepository reviewRepo;

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    ReviewPerListingServiceImpl reviewPerListing;

    String listingId;

    @BeforeEach
    void setup() {
        listingId = UUID.randomUUID().toString();
    }

    @Test
    public void testFindByListingId() {
        List<Review> reviews = new ArrayList<>();
        Review review1 = new Review();
        Review review2 = new Review();
        reviews.add(review1);
        reviews.add(review2);

        when(reviewRepo.findByListingId(listingId)).thenReturn(reviews);
        List<Review> reviewsFound = reviewPerListing.getReviews(listingId);
        assertEquals(reviews, reviewsFound);
        assertEquals(review1, reviewsFound.getFirst());
        assertEquals(review2, reviewsFound.get(1));
    }

    @Test
    public void testGetAverage() {
        Double avg = 7.5;
        when(reviewRepo.findAverageRating(listingId)).thenReturn(avg);
        Double avgFound = reviewPerListing.averageRating(listingId);
        assertEquals(avg, avgFound);
    }

    @Test
    public void testDeleteReviews() {
        try {
            CompletableFuture<Void> something = reviewPerListing.deleteReviewInListing(listingId);
            something.get();
            verify(reviewRepo).deleteByListingId(listingId);
        }
        catch (Exception e) {
            fail();
        }
    }
}
