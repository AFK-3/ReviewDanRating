package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewAndRatingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewByListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                                               @RequestHeader("Authorization") String username,
                                               @RequestBody ReviewAndRating reviewAndRating) {
        try {
            reviewAndRatingService.create(listingId, username,
                    reviewAndRating.getReview(), reviewAndRating.getRating());

            return ResponseEntity.ok("Review Successfully Added to listing");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/allowUserToReview")
    public ResponseEntity<String> allowUserToReview(@RequestHeader("Authorization") String username,
                                                    @RequestBody Map<String, String> map) {

        if (!username.equals("admin")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non admin can't allow user to review");
        }

        try {
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
                                               @RequestHeader("Authorization") String username,
                                               @RequestBody ReviewAndRating reviewAndRating) {
        try {
            reviewAndRatingService.update(listingId, username, reviewAndRating);

            return ResponseEntity.ok("Review Successfully modified");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteReview/{listingId}")
    public ResponseEntity<String> deleteReview (@PathVariable("listingId") String listingId,
                                                @RequestHeader("Authorization") String username) {
        try {
            reviewAndRatingService.delete(listingId, username);
            return ResponseEntity.ok("Review Successfully Deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
