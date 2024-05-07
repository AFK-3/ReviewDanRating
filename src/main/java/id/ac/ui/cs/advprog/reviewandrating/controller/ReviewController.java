package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewPerListingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewPerListingService reviewPerListingService;
    private String urlApiGateaway = "localhost:8080";



    @GetMapping("/getReview/{listingId}")
    public ResponseEntity<Model> getReview(Model model, @PathVariable("listingId") String listingId,
                                           @RequestHeader("Authorization") String token) {
        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            Double avg = reviewPerListingService.averageRating(listingId);
            model.addAttribute("average_rating", avg);

            List<Review> reviews = reviewPerListingService.getReviews(listingId);
            model.addAttribute("reviews", reviews);

            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/createReview/{listingId}")
    public ResponseEntity<Review> createReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review review) {

        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = getUsernameFromToken(token);
            review = reviewService.create(listingId, username,
                    review.getDescription(), review.getRating());

            return ResponseEntity.ok(review);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/allowUserToReview")
    public ResponseEntity<String> allowUserToReview(@RequestHeader("Authorization") String token,
                                                    @RequestBody Map<String, String> map) {

        try {
            String listingId = map.get("listingId");
            String username = map.get("username");
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String role = getRoleFromToken(token);
            System.out.println(role);
            if (!role.equals("STAFF")) {
                throw new Exception("Non STAFF can't allow user to review");
            }

            reviewService.allowToReview(listingId, username);

            return ResponseEntity.ok(String.format("Allow %s to review listing with id %s",
                    username, listingId));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateReview/{listingId}")
    public ResponseEntity<Review> updateReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review modifiedReview) {
        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = getUsernameFromToken(token);
            Review newReview = reviewService.update(listingId, username, modifiedReview);

            return ResponseEntity.ok(newReview);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteReview/{listingId}")
    public ResponseEntity<Review> deleteReview (@PathVariable("listingId") String listingId,
                                                @RequestHeader("Authorization") String token) {
        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = getUsernameFromToken(token);
            Review deletedReview = reviewService.delete(listingId, username);

            return ResponseEntity.ok(deletedReview);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getUsernameFromToken(String token) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-username", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }

    private String getRoleFromToken(String token) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-role", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }
}
