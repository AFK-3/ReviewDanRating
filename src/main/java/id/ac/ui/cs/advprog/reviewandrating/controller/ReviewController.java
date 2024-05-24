package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.reviewandrating.middleware.ListingMIddleware;
import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewPerListingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class ReviewController {
    private final ReviewService reviewService;

    private final ReviewPerListingService reviewPerListingService;

    AuthMiddleware authMiddleware;

    ListingMIddleware listingMIddleware;

    @Autowired
    public ReviewController(ReviewService reviewService, ReviewPerListingService reviewPerListingService,
                            AuthMiddleware authMiddleware, ListingMIddleware listingMIddleware) {
        this.reviewService = reviewService;
        this.reviewPerListingService = reviewPerListingService;
        this.authMiddleware = authMiddleware;
        this.listingMIddleware = listingMIddleware;
    }

    @GetMapping("/getReview/{listingId}")
    public ResponseEntity<Model> getReview(Model model, @PathVariable("listingId") String listingId,
                                           @RequestHeader("Authorization") String token) {
        try {
            if (!listingMIddleware.isListingExist(listingId, token)) {
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
            if (e instanceof  RestClientException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/createReview/{listingId}")
    public ResponseEntity<Review> createReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review review) {

        try {
            if (!listingMIddleware.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = authMiddleware.getUsernameFromToken(token);
            review = reviewService.create(listingId, username,
                    review.getDescription(), review.getRating());

            return ResponseEntity.ok(review);
        }
        catch (Exception e) {
            if (e instanceof  RestClientException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/allowUserToReview")
    public ResponseEntity<String> allowUserToReview(@RequestHeader("Authorization") String token,
                                                    @RequestBody Map<String, String> map) {

        try {
            String listingId = map.get("listingId");
            String username = map.get("username");
            if (!listingMIddleware.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String role = authMiddleware.getRoleFromToken(token);
            if (!role.equals("STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only STAFF can allow user to review");
            }

            reviewService.allowToReview(listingId, username);

            return ResponseEntity.ok(String.format("Allow %s to review listing with id %s",
                    username, listingId));
        }
        catch(Exception e) {
            if (e instanceof  RestClientException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateReview/{listingId}")
    public ResponseEntity<Review> updateReview(@PathVariable("listingId") String listingId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Review modifiedReview) {
        try {
            if (!listingMIddleware.isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = authMiddleware.getUsernameFromToken(token);
            Review newReview = reviewService.update(listingId, username, modifiedReview);

            return ResponseEntity.ok(newReview);
        }
        catch (Exception e) {
            if (e instanceof  RestClientException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteReview/{listingId}")
    public ResponseEntity<Review> deleteReview (@PathVariable("listingId") String listingId,
                                                @RequestHeader("Authorization") String token) {
        try {
            if (!listingMIddleware  .isListingExist(listingId, token)) {
                reviewPerListingService.deleteReviewInListing(listingId);
                return ResponseEntity.notFound().build();
            }

            String username = authMiddleware.getUsernameFromToken(token);
            Review deletedReview = reviewService.delete(listingId, username);

            return ResponseEntity.ok(deletedReview);
        }
        catch (Exception e) {
            if (e instanceof  RestClientException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
}
