package id.ac.ui.cs.advprog.reviewandrating.service;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;
import id.ac.ui.cs.advprog.reviewandrating.service.command.ReviewableCommand;
import org.springframework.stereotype.Component;

@Component
public class ReviewableInvoker {
    ReviewableCommand reviewableCommand;

    public void setCommand(ReviewableCommand reviewableCommand) {
        this.reviewableCommand = reviewableCommand;
    }

    public Reviewable executeCommand(String listingId, String token) {
        if (reviewableCommand == null) {
            throw new NullPointerException("Command not set");
        }

        return reviewableCommand.execute(listingId, token);
    }
}
