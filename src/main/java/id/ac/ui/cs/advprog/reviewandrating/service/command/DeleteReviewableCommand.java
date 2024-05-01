package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DeleteReviewableCommand extends ReviewableCommand{
    private ReviewableRepository reviewableRepository;

    private ListingDummyRepository listingDummyRepo;

    public DeleteReviewableCommand(ReviewableRepository reviewableRepository, ListingDummyRepository listingDummyRepo) {
        this.listingDummyRepo = listingDummyRepo;
        this.reviewableRepository = reviewableRepository;
    }

    public Reviewable execute(String listingId, String token) {
        ResponseEntity<String> response = this.getListing(listingId, token);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException("Can't delete reviewable from existing listing");
        }

        return reviewableRepository.delete(listingId);
    }
}
