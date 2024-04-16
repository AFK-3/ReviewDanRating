package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewAndRatingBuilder;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewableBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewAndRatingRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewAndRatingServiceImpl implements ReviewAndRatingService {

    private final ReviewableRepository reviewableRepo;
    private final ReviewAndRatingRepository reviewAndRatingRepo;
    private final ReviewableService reviewableService;

    private final ReviewAndRatingBuilder reviewBuilder = new ReviewAndRatingBuilder();

    @Autowired
    public ReviewAndRatingServiceImpl(ReviewableRepository reviewableRepo,
                                 ReviewAndRatingRepository reviewAndRatingRepo,
                                      ReviewableService reviewableService) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
        this.reviewableService = reviewableService;
    }
    public ReviewAndRating create(String listingId, String username, String review, int rating){
        Reviewable reviewable = reviewableService.getReviewable(listingId);
        if (reviewable == null)
            return null;

        ReviewAndRating reviewAndRating = reviewBuilder.reset()
                .addId()
                .addRating(rating)
                .addReview(review)
                .addWriter(username)
                .build();

        reviewAndRatingRepo.put(reviewAndRating);
        reviewable.getReviews().put(username, reviewAndRating);
        reviewableRepo.put(reviewable);
        return reviewAndRating;
    }
    public ReviewAndRating findById(String id){
        return reviewAndRatingRepo.findById(id);
    }
    public ReviewAndRating update(String listingId, ReviewAndRating modifiedReviewAndRating){
        Reviewable reviewable = reviewableService.getReviewable(listingId);
        if (reviewable == null)
            return null;

        reviewAndRatingRepo.put(modifiedReviewAndRating);
        reviewable.getReviews().put(modifiedReviewAndRating.getWriter(), modifiedReviewAndRating);
        reviewableRepo.put(reviewable);
        return modifiedReviewAndRating;

    }
    public ReviewAndRating delete(String listingId){
        return null;
    }
}
