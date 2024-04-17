package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewableBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewAndRatingRepository;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class ReviewableServiceImpl implements ReviewableService{
    private ReviewableRepository reviewableRepo;
    private ReviewAndRatingRepository reviewAndRatingRepo;

    private final ReviewableBuilder reviewableBuilder = new ReviewableBuilder();

    @Autowired
    public ReviewableServiceImpl(ReviewableRepository reviewableRepo,
                                 ReviewAndRatingRepository reviewAndRatingRepo) {
        this.reviewableRepo = reviewableRepo;
        this.reviewAndRatingRepo = reviewAndRatingRepo;
    }
    public Reviewable open(String listingId){
        Reviewable reviewable = reviewableRepo.findByListingId(listingId);
        if (reviewable == null) {
            reviewable = reviewableBuilder.reset()
                    .addListingId(UUID.fromString(listingId))
                    .addReviews(new HashMap<>())
                    .build();
            reviewableRepo.put(reviewable);
        }
        return reviewable;
    }

    public Reviewable close(String listingId) {
        Reviewable reviewable = reviewableRepo.delete(listingId);
        if (reviewable != null) {
            for (ReviewAndRating review : reviewable.getReviews().values()) {
                reviewAndRatingRepo.delete(review.getId().toString());
            }
        }
        return reviewable;
    }

    //Jika listing sudah di delete, maka return null
    public Reviewable getReviewable(String listingId) {
        //Cari tau listing ada atau tidak
        boolean isListingExist = listingId.equals("1");
        if (isListingExist) {
            return this.open(listingId);
        }
        else {
            this.close(listingId);
            return null;
        }
    }

    public double getAverageRating(String listingId){
        Reviewable reviewable = open(listingId);
        double average = 0;
        int counter = 0;
        for (ReviewAndRating review : reviewable.getReviews().values()) {
            average += review.getRating();
            counter++;
        }

        return counter != 0? average/counter : 0;
    }
}
