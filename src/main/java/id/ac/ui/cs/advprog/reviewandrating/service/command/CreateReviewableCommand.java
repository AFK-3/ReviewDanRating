package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewableBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component

public class CreateReviewableCommand implements ReviewableCommand{
    private ReviewableRepository reviewableRepository;
    private ReviewableBuilder reviewableBuilder = new ReviewableBuilder();

    private ListingDummyRepository listingDummyRepo;

    public CreateReviewableCommand(ReviewableRepository reviewableRepository, ListingDummyRepository listingDummyRepo) {
        this.listingDummyRepo = listingDummyRepo;
        this.reviewableRepository = reviewableRepository;
    }
    public Reviewable execute(String listingId) {
        if (listingDummyRepo.findById(listingId) == null) {
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
