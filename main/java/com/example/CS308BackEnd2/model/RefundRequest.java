package com.example.CS308BackEnd2.model;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refundRequest")
@EntityListeners(AuditingEntityListener.class)
public class RefundRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rrID")
    private Long id;

    @Column
    private Long orderID;

    @Column
    private Long productID;

    @Column
    private String productName;

    /*
    @Column
    private String reason;
    */

    public enum Status {PENDING, APPROVED, REJECTED};

    @Column
    private Status status = Status.PENDING;

    @Column
    @CreatedDate
    private Date date;

    @Column
    @CreatedBy
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "uID", referencedColumnName = "uID")
    private User user;

    public RefundRequest(Long orderID, Long productID, String productName) {
        this.orderID = orderID;
        this.productID = productID;
        this.productName = productName;
    }

    public RefundRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    /*
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }*/

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
