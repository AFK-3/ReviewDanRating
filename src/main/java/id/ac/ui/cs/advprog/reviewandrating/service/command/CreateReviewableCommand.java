package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewableBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component

public class CreateReviewableCommand extends ReviewableCommand{
    private ReviewableRepository reviewableRepository;
    private ReviewableBuilder reviewableBuilder = new ReviewableBuilder();

    private ListingDummyRepository listingDummyRepo;

    public CreateReviewableCommand(ReviewableRepository reviewableRepository, ListingDummyRepository listingDummyRepo) {
        this.listingDummyRepo = listingDummyRepo;
        this.reviewableRepository = reviewableRepository;
    }
    public Reviewable execute(String listingId, String token) {
        ResponseEntity<String> response = this.getListing(listingId, token);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            reviewableRepository.delete(listingId);
            throw new IllegalArgumentException("Listing doesn't exist");
        }

        if (reviewableRepository.findByListingId(listingId) != null) {
            throw new IllegalArgumentException("Listing already exist");
        }

        Reviewable reviewable = reviewableBuilder.reset()
                .addListingId(listingId)
                .addReviews(new HashMap<>())
                .build();
        reviewableRepository.save(reviewable);
        return reviewable;
    }
}
