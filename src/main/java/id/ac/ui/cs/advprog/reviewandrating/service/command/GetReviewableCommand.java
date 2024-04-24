package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetReviewableCommand implements ReviewableCommand{
    private ReviewableRepository reviewableRepository;

    private ListingDummyRepository listingDummyRepo;

    public GetReviewableCommand(ReviewableRepository reviewableRepository, ListingDummyRepository listingDummyRepo) {
        this.listingDummyRepo = listingDummyRepo;
        this.reviewableRepository = reviewableRepository;
    }

    public Reviewable execute(String listingId){
        if (listingDummyRepo.findById(listingId) == null) {
            reviewableRepository.delete(listingId);
            throw new IllegalArgumentException("Listing doesn't exist");
        }

        return reviewableRepository.findByListingId(listingId);
    }
}
