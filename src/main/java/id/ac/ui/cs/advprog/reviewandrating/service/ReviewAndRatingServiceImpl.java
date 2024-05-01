package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewAndRatingBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ListingDummyRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewAndRatingRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import id.ac.ui.cs.advprog.reviewandrating.service.command.CreateReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.DeleteReviewableCommand;
import id.ac.ui.cs.advprog.reviewandrating.service.command.GetReviewableCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewAndRatingServiceImpl implements ReviewAndRatingService {
    private final ReviewAndRatingRepository reviewAndRatingRepo;
    private final ReviewableRepository reviewableRepo;
    private final ListingDummyRepository listingDummyRepo;
    private final ReviewableInvoker reviewableInvoker;
    private final ReviewAndRatingBuilder reviewBuilder = new ReviewAndRatingBuilder();
    private GetReviewableCommand getReviewable;
    private CreateReviewableCommand createReviewable;

    @Autowired
    public ReviewAndRatingServiceImpl(ReviewableRepository reviewableRepo,
                                 ReviewAndRatingRepository reviewAndRatingRepo,
                                      ReviewableInvoker reviewableInvoker,
                                      ListingDummyRepository listingDummyRepo) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
        this.reviewableInvoker = reviewableInvoker;
        this.listingDummyRepo = listingDummyRepo;

        this.getReviewable = new GetReviewableCommand(reviewableRepo, listingDummyRepo);
        this.createReviewable = new CreateReviewableCommand(reviewableRepo, listingDummyRepo);
    }
    public ReviewAndRating create(String listingId, String username, String review, int rating, String token){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);
        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId, token);
        }

        if (!reviewable.getReviews().containsKey(username)) {
            throw new IllegalArgumentException("User doesn't have permission to review on this listing");
        }
        else if (reviewable.getReviews().get(username) != null) {
            throw new IllegalArgumentException("User already created review on this listing");
        }

        ReviewAndRating reviewAndRating = reviewBuilder.reset()
                .addId()
                .addRating(rating)
                .addReview(review)
                .addWriter(username)
                .build();
        reviewable.getReviews().put(username, reviewAndRating);
        reviewAndRatingRepo.save(reviewAndRating);

        return reviewAndRating;
    }
    public ReviewAndRating findById(String id){
        return reviewAndRatingRepo.findById(id);
    }

    public boolean isAlreadyReview(String listingId, String username, String token) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);

        return reviewable == null || reviewable.getReviews().get(username) == null;
    }
    public ReviewAndRating update(String listingId, String username, ReviewAndRating modifiedReviewAndRating, String token){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);
        if (isAlreadyReview(listingId, username, token)) {
            throw new IllegalArgumentException("You don't have review on this listing");
        }

        ReviewAndRating actualReview = reviewable.getReviews().get(username);
        actualReview.setReview(modifiedReviewAndRating.getReview());
        actualReview.setRating(modifiedReviewAndRating.getRating());
        return modifiedReviewAndRating;
    }
    public ReviewAndRating delete(String listingId, String username, String token){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);

        if (isAlreadyReview(listingId, username, token)) {
            throw new IllegalArgumentException("You don't have review on this listing");
        }

        ReviewAndRating reviewAndRating = reviewable.getReviews().put(username, null);
        reviewAndRatingRepo.delete(reviewAndRating.getId().toString());

        return reviewAndRating;
    }

    public void allowUserToReview(String username, String listingId, String token) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId, token);
        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId, token);
        }
        if (!reviewable.getReviews().containsKey(username)) {
            reviewable.getReviews().put(username, null);
        }
    }
}
