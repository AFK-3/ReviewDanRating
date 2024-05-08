package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class ReviewPerListingServiceImpl implements ReviewPerListingService{
    @Autowired
    private ReviewRepository reviewRepo;

    @Async
    public CompletableFuture<List<Review>> getReviews(String listingId) {
        List<Review> reviews = reviewRepo.findByListingId(listingId);
        List<Review> validReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getDescription() != null) {
                validReviews.add(review);
            }
        }
        return CompletableFuture.completedFuture(validReviews);
    }
    @Async
    public void deleteReviewInListing(String listingId) {
        reviewRepo.deleteByListingId(listingId);
    }

    @Async
    public CompletableFuture<Double> averageRating(String listingId) {
        Double avgRating = reviewRepo.findAverageRating();
        return CompletableFuture.completedFuture(avgRating);
    }
    @Async
    public CompletableFuture<Boolean> isListingExist(String listingId, String token) {
        String url = "http://localhost:8081/listing/get-by-id/" + listingId + "?listingId=" + listingId;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

            return CompletableFuture.completedFuture(true);
        }
        catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
}
