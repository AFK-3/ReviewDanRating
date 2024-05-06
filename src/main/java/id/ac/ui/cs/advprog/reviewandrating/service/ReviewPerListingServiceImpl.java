package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReviewPerListingServiceImpl implements ReviewPerListingService{
    @Autowired
    private ReviewRepository reviewRepo;

    public List<Review> getReviews(String listingId) {
        return reviewRepo.findByListingId(listingId);
    }
    public void deleteReviewInListing(String listingId) {
        reviewRepo.deleteByListingId(listingId);
    }
    public double averageRating(String listingId) {
        return reviewRepo.findAverageRating();
    }
    public boolean isListingExist(String listingId, String token) {
        String url = "http://localhost:8081/listing/get-by-id/" + listingId + "?listingId=" + listingId;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        return response.getStatusCode() != HttpStatus.NOT_FOUND;
    }
}
