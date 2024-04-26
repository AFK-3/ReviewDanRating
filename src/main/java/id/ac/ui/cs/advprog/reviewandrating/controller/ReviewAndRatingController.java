package id.ac.ui.cs.advprog.reviewandrating.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewAndRatingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewByListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ReviewAndRatingController {
    private ReviewByListingService reviewByListingService;
    private ReviewAndRatingService reviewAndRatingService;
    private String urlApiGateaway = "localhost:8080";

    @Autowired
    public ReviewAndRatingController(ReviewByListingService reviewByListingService,
                                     ReviewAndRatingService reviewAndRatingService) {
        this.reviewByListingService = reviewByListingService;
        this.reviewAndRatingService = reviewAndRatingService;
    }

    @GetMapping("/getReview/{listingId}")
    public ResponseEntity<Model> getReview(Model model, @PathVariable("listingId") String listingId) {
        try {
            Double avg = reviewByListingService.getAverageRating(listingId);
            model.addAttribute("average_rating", avg);

            List<ReviewAndRating> reviews = reviewByListingService.getReviewByListing(listingId);
            model.addAttribute("reviews_ratings", reviews);

            return ResponseEntity.ok(model);
        }
        catch (Exception e) {
            model.addAttribute("Error Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @PostMapping("/createReview/{listingId}")
    public ResponseEntity<String> createReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody ReviewAndRating reviewAndRating) {

        try {
            HashMap<String, String> user = getUserFromAuth(token);
            reviewAndRatingService.create(listingId, user.get("username"),
                    reviewAndRating.getReview(), reviewAndRating.getRating());

            return ResponseEntity.ok(String.format("Review by %s Successfully Added to listing",
                    user.get("username")));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/allowUserToReview")
    public ResponseEntity<String> allowUserToReview(@RequestHeader("Authorization") String token,
                                                    @RequestBody Map<String, String> map) {

        try {
            HashMap<String, String> user = getUserFromAuth(token);
            if (!user.get("type").equals("STAFF")) {
                throw new Exception("Non STAFF can't allow user to review");
            }
            reviewAndRatingService.allowUserToReview(map.get("username"), map.get("listingId"));

            return ResponseEntity.ok(String.format("Allow %s to review listing with id %s",
                    map.get("username"), map.get("listingId")));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateReview/{listingId}")
    public ResponseEntity<String> updateReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody ReviewAndRating reviewAndRating) {
        try {
            HashMap<String, String> user = getUserFromAuth(token);
            reviewAndRatingService.update(listingId, user.get("username"), reviewAndRating);

            return ResponseEntity.ok("Review Successfully modified");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteReview/{listingId}")
    public ResponseEntity<String> deleteReview (@PathVariable("listingId") String listingId,
                                                @RequestHeader("Authorization") String token) {
        try {
            HashMap<String, String> user = getUserFromAuth(token);
            reviewAndRatingService.delete(listingId, user.get("username"));
            return ResponseEntity.ok("Review Successfully Deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private HashMap<String, String> getUserFromAuth(String token) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/auth/get-user", HttpMethod.GET, httpEntity, String.class);


        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), new TypeReference<HashMap<String, String>>() {});
    }
}
