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

    @Autowired
    RestTemplate restTemplate;
    private String urlApiGateaway = "http://35.198.243.155/";

    public List<Review> getReviews(String listingId) {
        return reviewRepo.findByListingId(listingId);
    }
    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> deleteReviewInListing(String listingId) {
        reviewRepo.deleteByListingId(listingId);
        System.out.println(Thread.currentThread().toString());
        return CompletableFuture.completedFuture(null);
    }

    public Double averageRating(String listingId) {
        Double avgRating = reviewRepo.findAverageRating(listingId);
        return avgRating;
    }

}
