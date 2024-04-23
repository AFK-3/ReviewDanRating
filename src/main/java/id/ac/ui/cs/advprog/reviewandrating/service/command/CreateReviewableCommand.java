package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewableBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;

import java.util.HashMap;
import java.util.UUID;

public class CreateReviewableCommand implements ReviewableCommand{
    private ReviewableRepository reviewableRepository;
    private ReviewableBuilder reviewableBuilder = new ReviewableBuilder();

    public CreateReviewableCommand(ReviewableRepository reviewableRepository) {
        this.reviewableRepository = reviewableRepository;
    }
    public Reviewable execute(String listingId) {
        boolean isListingExist = listingId.equals("1");
        if (!isListingExist) {
            reviewableRepository.delete(listingId);
            throw new IllegalArgumentException("Listing doesn't exist");
        }

        if (reviewableRepository.findByListingId(listingId) != null) {
            throw new IllegalArgumentException("Listing already exist");
        }

        Reviewable reviewable = reviewableBuilder.reset()
                .addListingId(UUID.fromString(listingId))
                .addReviews(new HashMap<>())
                .build();
        reviewableRepository.save(reviewable);
        return reviewable;
    }
}
