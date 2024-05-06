package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewPerListingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
                throw new Exception("Listing doesn't exists!");
            }

            Double avg = reviewPerListingService.averageRating(listingId);
            model.addAttribute("average_rating", avg);

            List<Review> reviews = reviewPerListingService.getReviews(listingId);
            model.addAttribute("reviews_ratings", reviews);

            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            model.addAttribute("Error Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @PostMapping("/createReview/{listingId}")
    public ResponseEntity<Model> createReview(Model model, @PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review review) {

        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                throw new Exception("Listing doesn't exists!");
            }

            String username = getUsernameFromToken(token);
            review = reviewService.create(listingId, username,
                    review.getDescription(), review.getRating());

            model.addAttribute("review", review);

            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            model.addAttribute("Error Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
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
                throw new Exception("Listing doesn't exists!");
            }

            String role = getRoleFromToken(token);
            if (!role.equals("STAFF")) {
                throw new Exception("Non STAFF can't allow user to review");
            }

            reviewService.allowToReview(listingId, username);

            return ResponseEntity.ok(String.format("Allow %s to review listing with id %s",
                    username, listingId));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateReview/{listingId}")
    public ResponseEntity<Model> updateReview(Model model, @PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review modifiedReview) {
        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                throw new Exception("Listing doesn't exists!");
            }

            String username = getUsernameFromToken(token);
            modifiedReview = reviewService.update(listingId, username, modifiedReview);

            model.addAttribute("new_review", modifiedReview);
            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            model.addAttribute("Error Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @DeleteMapping("/deleteReview/{listingId}")
    public ResponseEntity<Model> deleteReview (Model model, @PathVariable("listingId") String listingId,
                                                @RequestHeader("Authorization") String token) {
        try {
            if (!reviewPerListingService.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                throw new Exception("Listing doesn't exists!");
            }

            String username = getUsernameFromToken(token);
            Review deletedReview = reviewService.delete(listingId, username);

            model.addAttribute("deleted_review", deletedReview);
            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            model.addAttribute("Error Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    private String getUsernameFromToken(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-username", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }

    private String getRoleFromToken(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-role", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }
}
