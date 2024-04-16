package id.ac.ui.cs.advprog.reviewandrating.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class ReviewAndRating {
    private UUID id;
    private String writer;
    private String review;
    private int rating;
}
