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
public class ReviewPerListingServiceImpl implements ReviewPerListingService{
    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    RestTemplate restTemplate;
    private String urlApiGateaway = "http://35.198.243.155/";

    public List<Review> getReviews(String listingId) {
        return reviewRepo.findByListingId(listingId);
    }
    @Async("Executor")
    public CompletableFuture<Void> deleteReviewInListing(String listingId) {
        reviewRepo.deleteByListingId(listingId);
        return CompletableFuture.completedFuture(null);
    }

    public Double averageRating(String listingId) {
        Double avgRating = reviewRepo.findAverageRating(listingId);
        return avgRating;
    }

    public Boolean isListingExist(String listingId, String token) {
        String url = "http://localhost:8081/listing/get-by-id/" + listingId + "?listingId=" + listingId;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
