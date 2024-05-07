package id.ac.ui.cs.advprog.reviewandrating.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReviewId implements Serializable {
    private String username;
    private String listingId;
}
