package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    public List<Invoice> findByUserId(Long id);

    public Invoice findByOrderId(long orderId);

    public List<Invoice> findByTotalPriceIsNot(double price);

}
