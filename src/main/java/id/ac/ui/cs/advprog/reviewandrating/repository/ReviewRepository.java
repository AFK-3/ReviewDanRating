package id.ac.ui.cs.advprog.reviewandrating.repository;

import id.ac.ui.cs.advprog.reviewandrating.model.Review;
import id.ac.ui.cs.advprog.reviewandrating.model.ReviewId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    @Query("SELECT r FROM Review r WHERE r.listingId = :listingId AND r.description IS NOT NULL")
    List<Review> findByListingId(@Param("listingId") String listingId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.listingId = :listingId")
    void deleteByListingId(@Param("listingId") String listingId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.listingId = :listingId AND r.description IS NOT NULL")
    Double findAverageRating(@Param("listingId") String listingId);
}
