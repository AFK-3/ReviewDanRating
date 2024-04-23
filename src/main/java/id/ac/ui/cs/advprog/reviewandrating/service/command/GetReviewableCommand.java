package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;

public class GetReviewableCommand implements ReviewableCommand{
    private ReviewableRepository reviewableRepository;

    public GetReviewableCommand(ReviewableRepository reviewableRepository) {
        this.reviewableRepository = reviewableRepository;
    }

    public Reviewable execute(String listingId){
        boolean isListingExist = listingId.equals("1");
        if (!isListingExist) {
            reviewableRepository.delete(listingId);
            throw new IllegalArgumentException("Listing doesn't exist");
        }

        return reviewableRepository.findByListingId(listingId);
    }
}
