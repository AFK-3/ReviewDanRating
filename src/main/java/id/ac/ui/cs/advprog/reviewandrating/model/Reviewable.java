package id.ac.ui.cs.advprog.reviewandrating.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class Reviewable {
    private String listingId;
    private Map<String, ReviewAndRating> reviews;
}
