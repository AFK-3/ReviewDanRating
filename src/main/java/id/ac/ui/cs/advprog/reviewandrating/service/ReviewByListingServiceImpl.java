package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewAndRatingRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import id.ac.ui.cs.advprog.reviewandrating.service.command.CreateReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.DeleteReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.GetReviewableCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewByListingServiceImpl implements ReviewByListingService{
    private final ReviewAndRatingRepository reviewAndRatingRepo;
    private final ReviewableRepository reviewableRepo;
    private final ListingDummyRepository listingDummyRepo;
    private final ReviewableInvoker reviewableInvoker;
    private GetReviewableCommand getReviewable;
    private CreateReviewableCommand createReviewable;
    private DeleteReviewableCommand deleteReviewable;

    @Autowired
    public ReviewByListingServiceImpl(ReviewableRepository reviewableRepo,
                                      ReviewAndRatingRepository reviewAndRatingRepo,
                                      ReviewableInvoker reviewableInvoker,
                                      ListingDummyRepository listingDummyRepo) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
        this.reviewableInvoker = reviewableInvoker;
        this.listingDummyRepo = listingDummyRepo;

        this.getReviewable = new GetReviewableCommand(reviewableRepo, listingDummyRepo);
        this.createReviewable = new CreateReviewableCommand(reviewableRepo, listingDummyRepo);
        this.deleteReviewable = new DeleteReviewableCommand(reviewableRepo, listingDummyRepo);
    }

    public List<ReviewAndRating> deleteAllReviewInListing(String listingId, String token) {
        reviewableInvoker.setCommand(deleteReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            reviewAndRatingRepo.delete(review.getId().toString());
        }

        return reviewable.getReviews().values().stream().toList();
    }

    public Double getAverageRating(String listingId, String token) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);

        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId, token);
        }

        double average = 0;
        int counter = 0;
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            if (review == null) continue;
            average += review.getRating();
            counter++;
        }

        return counter != 0? average/counter : 0;
    }

    public List<ReviewAndRating> getReviewByListing(String listingId, String token) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);
        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId, token);
        }
        List<ReviewAndRating> reviews = new ArrayList<>();
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            if (review == null) continue;
            reviews.add(review);
        }

        return reviews;
    }
}
