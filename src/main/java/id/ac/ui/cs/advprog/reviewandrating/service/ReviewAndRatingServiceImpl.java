package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewAndRatingBuilder;
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
    private final ReviewableInvoker reviewableInvoker;
    private final ReviewAndRatingBuilder reviewBuilder = new ReviewAndRatingBuilder();
    private GetReviewableCommand getReviewable;
    private CreateReviewableCommand createReviewable;
    private DeleteReviewableCommand deleteReviewable;

    @Autowired
    public ReviewAndRatingServiceImpl(ReviewableRepository reviewableRepo,
                                 ReviewAndRatingRepository reviewAndRatingRepo,
                                      ReviewableInvoker reviewableInvoker) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
        this.reviewableInvoker = reviewableInvoker;

        this.getReviewable = new GetReviewableCommand(reviewableRepo);
        this.createReviewable = new CreateReviewableCommand(reviewableRepo);
        this.deleteReviewable = new DeleteReviewableCommand(reviewableRepo);
    }
    public ReviewAndRating create(String listingId, String username, String review, int rating){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);
        if (reviewable == null) {
            reviewableInvoker.setCommand(createReviewable);
            reviewable = reviewableInvoker.executeCommand(listingId);
        }
        else if (!reviewable.getReviews().containsKey(username)) {
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

    public boolean isAlreadyReview(String listingId, String username) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);

        return reviewable != null && reviewable.getReviews().get(username) != null;
    }
    public ReviewAndRating update(String listingId, ReviewAndRating modifiedReviewAndRating){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);
        if (isAlreadyReview(listingId, modifiedReviewAndRating.getWriter())) {
            throw new IllegalArgumentException("You don't have review on this listing");
        }

        reviewAndRatingRepo.save(modifiedReviewAndRating);
        reviewable.getReviews().put(modifiedReviewAndRating.getWriter(), modifiedReviewAndRating);
        return modifiedReviewAndRating;
    }
    public ReviewAndRating delete(String id, String listingId){
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);

        if (isAlreadyReview(listingId, reviewAndRatingRepo.findById(listingId).getWriter())) {
            throw new IllegalArgumentException("You don't have review on this listing");
        }

        ReviewAndRating reviewAndRating = reviewAndRatingRepo.delete(id);
        reviewable.getReviews().remove(reviewAndRating.getWriter());

        return reviewAndRating;
    }

    public void allowUserToReview(String username, String listingId) {
        reviewableInvoker.setCommand(getReviewable);
        Reviewable reviewable = reviewableInvoker.executeCommand(listingId);
        if (!reviewable.getReviews().containsKey(username)) {
            reviewable.getReviews().put(username, null);
        }
    }
}
