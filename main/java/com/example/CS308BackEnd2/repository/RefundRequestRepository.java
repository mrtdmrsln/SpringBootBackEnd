package com.example.CS308BackEnd2.repository;


import com.example.CS308BackEnd2.model.RefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest,Long> {
}
