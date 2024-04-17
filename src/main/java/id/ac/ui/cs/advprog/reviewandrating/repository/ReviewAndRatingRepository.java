package id.ac.ui.cs.advprog.reviewandrating.repository;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ReviewAndRatingRepository {
    private final Map<String, ReviewAndRating> reviewMap = new HashMap<>();

    public ReviewAndRating put(ReviewAndRating reviewAndRating) {
        reviewMap.put(reviewAndRating.getId().toString(), reviewAndRating);
        return reviewAndRating;
    }

    public ReviewAndRating findById(String id){
        return reviewMap.get(id);
    }

    public Collection<ReviewAndRating> findAll(){
        return reviewMap.values();
    }

    public ReviewAndRating delete(String id) {
        if (reviewMap.containsKey(id)) {
            return reviewMap.remove(id);
        }
        else {
            return null;
        }
    }
}
