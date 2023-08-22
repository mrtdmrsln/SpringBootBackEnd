package com.example.CS308BackEnd2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.util.Date;


@Entity
@Table(name = "comment")
@PrimaryKeyJoinColumn(name = "rID")
public class Comment extends Review{

    @Column(name = "cText")
    private String text = null;

    public enum Status {APPROVED, PENDING, REJECTED};

    @Column(name = "status")
    private Status status = Status.PENDING;

    @ManyToOne( optional = false) // EAGER is used to prevent lazy initialization exception, for avoiding infinite recursion
    @JoinColumn(name = "uID", nullable = false, referencedColumnName = "uID")
    //@JsonManagedReference // to prevent infinite recursion
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pID", nullable = false, referencedColumnName = "pID")
    @JsonBackReference // to prevent infinite recursion
    private Product product;

    public Comment() {
    }
    public Comment(Date date, int rating, String text, Status status) {
        super(date, rating);
        this.text = text;
        this.status = status;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                '}';
    }
}
