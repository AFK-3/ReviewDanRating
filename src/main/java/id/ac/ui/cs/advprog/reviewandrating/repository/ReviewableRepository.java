package id.ac.ui.cs.advprog.reviewandrating.repository;

import id.ac.ui.cs.advprog.reviewandrating.model.ReviewAndRating;
import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ReviewableRepository {
    private Map<String, Reviewable> reviewableMap = new HashMap<>();

    public Reviewable put(Reviewable reviewable) {
        reviewableMap.put(reviewable.getListingId().toString(), reviewable);
        return reviewable;
    }

    public Reviewable findByListingId(String listingId){
        return reviewableMap.get(listingId);
    }

    public Collection<Reviewable> findAll(){
        return reviewableMap.values();
    }

    public Reviewable delete(String listingId) {
        if (reviewableMap.containsKey(listingId)) {
            return reviewableMap.remove(listingId);
        }
        else {
            return null;
        }
    }
}
