package id.ac.ui.cs.advprog.reviewandrating.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class Reviewable {
    private UUID listingId;
    private Map<String, ReviewAndRating> reviews;
}
