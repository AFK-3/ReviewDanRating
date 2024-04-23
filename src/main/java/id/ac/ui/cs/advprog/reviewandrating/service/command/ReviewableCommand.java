package id.ac.ui.cs.advprog.reviewandrating.service.command;

import id.ac.ui.cs.advprog.reviewandrating.model.Reviewable;

public interface ReviewableCommand {
    public Reviewable execute(String listingId);
}
