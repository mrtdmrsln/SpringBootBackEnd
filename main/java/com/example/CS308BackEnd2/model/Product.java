package com.example.CS308BackEnd2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "pID")
    private long id;

    @Column(name = "pName")
    private String name;

    @Column(name = "pPrice")
    private double price;

    @Column(name = "pDiscPrice")
    private double discountedPrice = 0;

    @Column(name = "pDescription")
    private String description;

    @Column(name = "pImage")
    private URL image;

    @Column(name = "pStock")
    private long stock;

    @Column(name = "pDisRate")
    private double discountRate;

    public enum warranty {YES, NO};

    @Column(name = "pWarrantyStatus")
    private warranty warrantyStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "product")
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "product")
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "product")
    @JsonIgnore
    private List<ListItem> listItems = new ArrayList<>();

   /* @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();*/



    public void addCartItem(CartItem item) {
        this.cartItems.add(item);
        item.setProduct(this);
    }


    public void removeCartItem(CartItem item) {
        this.cartItems.remove(item);
        item.setProduct(null);
    }

    public void addListItem(ListItem item) {
        this.listItems.add(item);
        item.setProduct(this);
    }

    public void removeListItem(ListItem item) {
        this.listItems.remove(item);
        item.setProduct(null);
    }

   /* public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setProduct(this);
    }

    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setProduct(null);
    }*/


    public Product(String name, double price, String description, URL image, long stock, double discountRate, warranty warrantyStatus) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.stock = stock;
        this.discountRate = discountRate;
        this.warrantyStatus = warrantyStatus;
        this.discountedPrice = price*(1-discountRate);
    }

    public Product() {
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setProduct(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setProduct(null);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setReviews(List<Comment> comments) {
        this.comments = comments;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public warranty getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(warranty warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", stock=" + stock +
                ", discountRate=" + discountRate +
                ", warrantyStatus=" + warrantyStatus +
                '}';
    }
}

