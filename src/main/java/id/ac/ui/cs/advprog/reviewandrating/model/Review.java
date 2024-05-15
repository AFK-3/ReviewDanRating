package id.ac.ui.cs.advprog.reviewandrating.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@IdClass(ReviewId.class)
@Table(name = "Reviews")
public class Review {
    @Id
    @Column(name = "username")
    private String username;

    @Id
    @Column(name = "listingId")
    private String listingId;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private int rating;
}
