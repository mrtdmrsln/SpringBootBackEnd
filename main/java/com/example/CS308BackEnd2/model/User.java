package com.example.CS308BackEnd2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "userTable", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uEmail"})
})
public class User {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "uID")
    private long id;

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setUser(null);
    }

    public enum role {
        ROLE_CUSTOMER, ROLE_ADMIN, ROLE_SELLER,

        // give ROLE_CUSTOMER as default

    };
    public enum gender{MALE,FEMALE};

    @Column(name = "uName")
    private String name;

    @Column(name = "uAge")
    private int age;

    @Column(name = "uEmail")
    private String email;

    @Column(name = "uPassword")
    private String password;

    @Column(name = "uAddress")
    private String address;

    @Column(name = "uPhone")
    private String phone;

    @Column(name = "uRole")
    private role role;

    @Column(name = "uGender")
    private gender gender;

    @Column(name = "taxID")
    private Long taxID;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "user")
    @JsonBackReference
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.ALL,  mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;

    @OneToOne(cascade = CascadeType.ALL,  mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private WishList wishList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<RefundRequest> refundRequests;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public User(String name, int age, String email, String password, String address, String phone, User.role role, User.gender gender) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setUser(this);
    }

    public void addRefundRequest(RefundRequest refundRequest) {
        this.refundRequests.add(refundRequest);
        refundRequest.setUser(this);
    }

    public void removeRefundRequest(RefundRequest refundRequest) {
        this.refundRequests.remove(refundRequest);
        refundRequest.setUser(null);
    }

    public List<RefundRequest> getRefundRequests() {
        return refundRequests;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.role getRole() {
        return role;
    }

    public void setRole(User.role role) {
        this.role = role;
    }

    public User.gender getGender() {
        return gender;
    }

    public void setGender(User.gender gender) {
        this.gender = gender;
    }

    public Long getTaxID() {
        return taxID;
    }

    public void setTaxID() {
        this.taxID = 1000 + this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", gender=" + gender +
                '}';
    }
}

