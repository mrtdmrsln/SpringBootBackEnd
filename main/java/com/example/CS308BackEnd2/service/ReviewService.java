package com.example.CS308BackEnd2.service;




import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.repository.CommentRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;
import com.example.CS308BackEnd2.repository.ReviewRepository;
import com.example.CS308BackEnd2.model.Comment;
import com.example.CS308BackEnd2.model.Review;
import com.example.CS308BackEnd2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CommentRepository commentRepository,
                         UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createReview(Date date, int rating, String text, Comment.Status status) {

        Comment comment = new Comment(date, rating, text, status);

        commentRepository.save(comment);

        /*
        Review review = new Review();
        review.setDate(date);
        review.setRating(rating);
        //review.setText(text);*/

    }

    public Comment createReview(Comment comment, long userId, long productId) {
        Comment comment1 = new Comment(comment.getDate(), comment.getRating(), comment.getText(), comment.getStatus());
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist."));
        //user.addComment(comment1);
        //userRepository.save(user);

        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with id " + productId + " does not exist."));
        //product.addComment(comment1);

        //productRepository.save(product);
        comment1.setProduct(product);
        comment1.setUser(user);

        return commentRepository.save(comment1);

    }

    public String deleteReview(long id) {
        /*
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Review with id " + id + " does not exist."));

        User user = comment.getUser();
        user.removeComment(comment);
        userRepository.save(user);

        Product product = comment.getProduct();
        product.removeComment(comment);
        productRepository.save(product);*/

        commentRepository.deleteById(id);
        return "comment is deleted";
    }


    public Comment updateCommentStatus(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Comment with id " + id + " does not exist."));
        if (comment.getStatus() == Comment.Status.PENDING) {
            comment.setStatus(Comment.Status.APPROVED);
        } else if (comment.getStatus() == Comment.Status.APPROVED) {
            comment.setStatus(Comment.Status.REJECTED);
        } else {
            comment.setStatus(Comment.Status.PENDING);
        }
        return commentRepository.save(comment);
    }

    public Comment updateCommentStatusToApproved(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Comment with id " + id + " does not exist."));

        comment.setStatus(Comment.Status.APPROVED);

        return commentRepository.save(comment);
    }

    public Comment updateCommentStatusToRejected(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Comment with id " + id + " does not exist."));

        comment.setStatus(Comment.Status.REJECTED);

        return commentRepository.save(comment);
    }

    public Comment updateCommentStatusToPending(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Comment with id " + id + " does not exist."));

        comment.setStatus(Comment.Status.PENDING);

        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getApprovedComments() {
        return commentRepository.findAllByStatus(Comment.Status.APPROVED);
    }

    public List<Comment> getRejectedComments() {
        return commentRepository.findAllByStatus(Comment.Status.REJECTED);
    }

    public List<Comment> getPendingComments() {
        return commentRepository.findAllByStatus(Comment.Status.PENDING);
    }

    public List<Comment> getCommentsByProductId(long productId) {
        return commentRepository.findAllByProductId(productId);
    }
}

