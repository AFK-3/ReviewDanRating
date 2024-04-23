package id.ac.ui.cs.advprog.reviewandrating.controller;

import id.ac.ui.cs.advprog.reviewandrating.service.ReviewAndRatingService;
import id.ac.ui.cs.advprog.reviewandrating.service.ReviewableInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ReviewAndRatingController {
    private ReviewableInvoker reviewableInvoker;
    private ReviewAndRatingService reviewAndRatingService;

    @Autowired
    public ReviewAndRatingController(ReviewableInvoker reviewableInvoker,
                                     ReviewAndRatingService reviewAndRatingService) {
        this.reviewableInvoker = reviewableInvoker;
        this.reviewAndRatingService = reviewAndRatingService;
    }

    @GetMapping("/")
    public String createReviewPage(Model model) {
        return "Testing";
    }
}
