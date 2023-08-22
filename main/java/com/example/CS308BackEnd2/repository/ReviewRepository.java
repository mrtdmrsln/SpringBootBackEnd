package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //public Review findByCommentId(Long id);


}