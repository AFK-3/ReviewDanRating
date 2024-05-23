package id.ac.ui.cs.advprog.reviewandrating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class ReviewAndRatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewAndRatingApplication.class, args);
    }
}
