package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.service.ReviewAndRatingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ReviewAndRatingController {
    private ReviewableService reviewableService;
    private ReviewAndRatingService reviewAndRatingService;

    @Autowired
    public ReviewAndRatingController(ReviewableService reviewableService,
                                     ReviewAndRatingService reviewAndRatingService) {
        this.reviewableService = reviewableService;
        this.reviewAndRatingService = reviewAndRatingService;
    }

    @GetMapping("/")
    public String createReviewPage(Model model) {
        return "Testing";
    }
}
