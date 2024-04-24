package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewAndRatingRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import id.ac.ui.cs.advprog.reviewandrating.service.command.CreateReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.DeleteReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.GetReviewableCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewByListingServiceImpl implements ReviewAndRatingService{
    private final ReviewAndRatingRepository reviewAndRatingRepo;
    private final ReviewableRepository reviewableRepo;
    private final ReviewableInvoker reviewableInvoker;
    private GetReviewableCommand getReviewable;
    private CreateReviewableCommand createReviewable;
    private DeleteReviewableCommand deleteReviewable;

    @Autowired
    public ReviewByListingServiceImpl(ReviewableRepository reviewableRepo,
                                      ReviewAndRatingRepository reviewAndRatingRepo,
                                      ReviewableInvoker reviewableInvoker) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
        this.reviewableInvoker = reviewableInvoker;

        this.getReviewable = new GetReviewableCommand(reviewableRepo);
        this.createReviewable = new CreateReviewableCommand(reviewableRepo);
        this.deleteReviewable = new DeleteReviewableCommand(reviewableRepo);
    }

    public List<ReviewAndRating> deleteAllReviewInListing(String listingId) {
        reviewableInvoker.setCommand(deleteReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            reviewAndRatingRepo.delete(review.getId().toString());
        }

        return reviewable.getReviews().values().stream().toList();
    }

    public Double getAverageRating(String listingId) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);

        double average = 0;
        int counter = 0;
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            average += review.getRating();
            counter++;
        }

        return counter != 0? average/counter : 0;
    }

    public List<ReviewAndRating> getReviewByListing(String listingId) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);
        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId);
        }

        return reviewable.getReviews().values().stream().toList();
    }
}
