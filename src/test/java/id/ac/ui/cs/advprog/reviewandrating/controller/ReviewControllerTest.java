package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.reviewandrating.middleware.ListingMIddleware;
import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewPerListingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(ReviewController.class)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;
    @MockBean
    ReviewPerListingService reviewPerListingService;
    @MockBean
    AuthMiddleware authMiddleware;
    @MockBean
    ListingMIddleware listingMIddleware;
    @InjectMocks
    ReviewController reviewController;

    String validToken;
    String invalidToken;
    String validListing;
    String invalidListing;

    ReviewBuilder reviewBuilder;

    @BeforeEach
    void setUp() {
        validToken = "valid";
        invalidToken = "invalid";
        validListing = "123";
        invalidListing = "45";
        reviewBuilder = new ReviewBuilder();
    }

    @Test
    void testInvalidListingAllEndpoint() throws Exception{
        when(listingMIddleware.isListingExist(invalidListing, validToken)).thenReturn(false);

        mockMvc.perform(get(String.format("/getReview/%s", invalidListing))
                        .header("Authorization", validToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(post(String.format("/createReview/%s", invalidListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"description\": \"sangat bagus\", \"rating\": 10 }"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/allowUserToReview")
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"username\": \"hanau\", \"listingId\": \"45\" }"))
                        .andExpect(status().isNotFound());

        mockMvc.perform(put(String.format("/updateReview/%s", invalidListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{\"description\": \"bagus\", \"rating\": 5}"))
                        .andExpect(status().isNotFound());

        mockMvc.perform(delete(String.format("/deleteReview/%s", invalidListing))
                        .header("Authorization", validToken))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testInvalidTokenAllEndpoint() throws Exception {
        when(listingMIddleware.isListingExist(validListing, invalidToken)).thenThrow(RestClientException.class);

        mockMvc.perform(get(String.format("/getReview/%s", validListing))
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(String.format("/createReview/%s", validListing))
                        .header("Authorization", invalidToken)
                        .contentType("application/json")
                        .content("{ \"description\": \"sangat bagus\", \"rating\": 10 }"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/allowUserToReview")
                        .header("Authorization", invalidToken)
                        .contentType("application/json")
                        .content("{ \"username\": \"hanau\", \"listingId\": \"123\" }"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put(String.format("/updateReview/%s", validListing))
                        .header("Authorization", invalidToken)
                        .contentType("application/json")
                        .content("{\"description\": \"bagus\", \"rating\": 5}"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(String.format("/deleteReview/%s", validListing))
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testNonStaffAllowUserToReview() throws  Exception {
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getRoleFromToken(validToken)).thenReturn("SELLER");

        mockMvc.perform(post("/allowUserToReview")
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"username\": \"hanau\", \"listingId\": \"123\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Only STAFF can allow user to review"));
    }

    @Test
    void testAllowUserToReview() throws  Exception {
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getRoleFromToken(validToken)).thenReturn("STAFF");

        mockMvc.perform(post("/allowUserToReview")
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"username\": \"hanau\", \"listingId\": \"123\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Allow hanau to review listing with id %s",
                        validListing)));
    }

    @Test
    void testListReview() throws Exception{
        List<Review> reviews = new ArrayList<>();

        reviews.add(reviewBuilder.reset().addId(validListing, "hanau").
                addDescription("Bagus").addRating(10).build());

        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(reviewPerListingService.averageRating(validListing)).thenReturn(10.0);
        when(reviewPerListingService.getReviews(validListing)).thenReturn(reviews);

        mockMvc.perform(get(String.format("/getReview/%s", validListing))
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"average_rating\":10.0,\"reviews\":[{\"username\":\"hanau\",\"listingId\":\"123\",\"description\":\"Bagus\",\"rating\":10}]}"));
    }

    @Test
    void testCreateReview() throws  Exception{
        Review review = reviewBuilder.reset().addId(validListing, "hanau").
                addDescription("Bagus").addRating(10).build();
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.create(review.getListingId(), review.getUsername(), review.getDescription(),
                review.getRating())).thenReturn(review);

        mockMvc.perform(post(String.format("/createReview/%s", validListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"description\": \"Bagus\", \"rating\": 10 }"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"username\":\"hanau\",\"listingId\":\"123\",\"description\":\"Bagus\",\"rating\":10}"));
    }

    @Test
    void testCreateReviewBadRequest() throws Exception{
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.create(validListing, "hanau", "Bagus", 10)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post(String.format("/createReview/%s", validListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{ \"description\": \"Bagus\", \"rating\": 10 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateReview() throws Exception {
        Review modifiedReview = reviewBuilder.reset().addId(validListing, "hanau").
                addDescription("bagus").addRating(10).build();

        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.update(eq(validListing), eq("hanau"), ArgumentMatchers.argThat(review ->
                review != null &&
                review.getDescription().equals(modifiedReview.getDescription()) &&
                review.getRating() == modifiedReview.getRating()))).thenReturn(modifiedReview);

        mockMvc.perform(put(String.format("/updateReview/%s", validListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{\"description\": \"bagus\", \"rating\": 10}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"username\":\"hanau\",\"listingId\":\"123\",\"description\":\"bagus\",\"rating\":10}"));
    }

    @Test
    void testUpdateBadRequest() throws Exception{
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.update(eq(validListing), eq("hanau"), any())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put(String.format("/updateReview/%s", validListing))
                        .header("Authorization", validToken)
                        .contentType("application/json")
                        .content("{\"description\": \"bagus\", \"rating\": 10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteReview() throws  Exception{
        Review review = reviewBuilder.reset().addId(validListing, "hanau")
                        .addDescription("bagus").addRating(8).build();

        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.delete(validListing, "hanau")).thenReturn(review);

        mockMvc.perform(delete(String.format("/deleteReview/%s", validListing))
                        .header("Authorization", validToken)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"username\":\"hanau\",\"listingId\":\"123\",\"description\":\"bagus\",\"rating\":8}"));
    }

    @Test
    void testDeleteBadRequest() throws Exception{
        when(listingMIddleware.isListingExist(validListing, validToken)).thenReturn(true);
        when(authMiddleware.getUsernameFromToken(validToken)).thenReturn("hanau");
        when(reviewService.delete(validListing, "hanau")).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(delete(String.format("/deleteReview/%s", validListing))
                        .header("Authorization", validToken))
                .andExpect(status().isBadRequest());
    }
}
