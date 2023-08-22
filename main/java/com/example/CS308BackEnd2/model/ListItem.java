package com.example.CS308BackEnd2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "listItem")
public class ListItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "Price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "ProdID", referencedColumnName = "Pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "WLid", referencedColumnName = "WLid")
    @JsonIgnore
    private WishList wishList;

    public ListItem(Product product) {
        //this.product = product;
        this.price = product.getPrice();
    }

    public ListItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

}
