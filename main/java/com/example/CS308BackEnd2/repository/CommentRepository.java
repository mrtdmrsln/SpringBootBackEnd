package com.example.CS308BackEnd2.repository;



import com.example.CS308BackEnd2.model.Comment;
import com.example.CS308BackEnd2.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByStatus(Comment.Status status);

    List<Comment> findAllByProductId(long productId);
    //public Comment findCommentByReviewId(Long id);
}

