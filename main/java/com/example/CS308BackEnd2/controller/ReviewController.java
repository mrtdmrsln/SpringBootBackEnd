package com.example.CS308BackEnd2.controller;

import com.example.CS308BackEnd2.model.Comment;
import com.example.CS308BackEnd2.model.Review;
import com.example.CS308BackEnd2.service.ReviewService;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/review")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
@EnableJpaAuditing
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PutMapping("/comment/{rev_id}/update/approve")
    public ResponseEntity<Comment> updateCommentStatusToApproved(@PathVariable("rev_id") Long id){
        Comment updatedComment = reviewService.updateCommentStatusToApproved(id);
        return ResponseEntity.ok(updatedComment);
    }

    @PutMapping("/comment/{rev_id}/update/reject")
    public ResponseEntity<Comment> updateCommentStatusToRejected(@PathVariable("rev_id") Long id){
        Comment updatedComment = reviewService.updateCommentStatusToRejected(id);
        return ResponseEntity.ok(updatedComment);
    }

    @PutMapping("/comment/{rev_id}/update/pending")
    public ResponseEntity<Comment> updateCommentStatusToPending(@PathVariable("rev_id") Long id){
        Comment updatedComment = reviewService.updateCommentStatusToPending(id);
        return ResponseEntity.ok(updatedComment);
    }

    @GetMapping("/comment/all")
    public ResponseEntity<List<Comment>> getAllComments(){
        return ResponseEntity.ok(reviewService.getAllComments());
    }

    @GetMapping("/comment/approved")
    public ResponseEntity<List<Comment>> getApprovedComments(){
        return ResponseEntity.ok(reviewService.getApprovedComments());
    }

    @GetMapping("/comment/pending")
    public ResponseEntity<List<Comment>> getPendingComments(){
        return ResponseEntity.ok(reviewService.getPendingComments());
    }

    @GetMapping("/comment/rejected")
    public ResponseEntity<List<Comment>> getRejectedComments(){
        return ResponseEntity.ok(reviewService.getRejectedComments());
    }

    @GetMapping("/comment/{Prod_id}")
    public ResponseEntity<List<Comment>> getCommentsByProductId(@PathVariable("Prod_id") Long id){
        return ResponseEntity.ok(reviewService.getCommentsByProductId(id));
    }


    @PostMapping("/comment/create/{user_id}/{prod_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Comment> createComment(@PathVariable("user_id") Long user_id, @RequestBody Comment comment,
                                                @PathVariable ("prod_id") Long prod_id){
        Comment comment1 = reviewService.createReview(comment, user_id, prod_id);
        return ResponseEntity.ok(comment1);
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") long id){
        String deletedReview = reviewService.deleteReview(id);
        return ResponseEntity.ok(deletedReview);
    }


}

