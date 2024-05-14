package id.ac.ui.cs.advprog.reviewandrating.Repository;

import id.ac.ui.cs.advprog.reviewandrating.model.builder.ReviewBuilder;
import id.ac.ui.cs.advprog.reviewandrating.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReviewRepoTest {

    @Autowired
    ReviewRepository reviewRepository;
    ReviewBuilder reviewBuilder;

    @BeforeEach
    public void setUp() {
        reviewBuilder = new ReviewBuilder().reset();
    }
}
