package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;

public class DeleteReviewableCommand implements ReviewableCommand{
    private ReviewableRepository reviewableRepository;

    public DeleteReviewableCommand(ReviewableRepository reviewableRepository) {
        this.reviewableRepository = reviewableRepository;
    }

    public Reviewable execute(String listingId) {
        boolean isListingExist = listingId.equals("1");
        if (isListingExist) {
            throw new IllegalArgumentException("Can't delete reviewable from existing listing");
        }

        return reviewableRepository.delete(listingId);
    }
}
