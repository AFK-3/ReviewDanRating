package id.ac.ui.cs.advprog.reviewandrating.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Reviewable {
    private UUID listingId;
    private List<ReviewAndRating> reviews;
}
