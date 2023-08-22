package com.example.CS308BackEnd2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "cartItem")
public class CartItem {
    @Id
    @Column(name = "CIid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity = 1;

    @Column(name = "price")
    private double price;

    @Column(name = "totalPrice")
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "ProdID", referencedColumnName = "Pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "SCid", referencedColumnName = "SCid")
    @JsonIgnore
    private ShoppingCart shoppingCart;

    public CartItem( Product product) {
        //this.product = product;
        this.price = product.getDiscountedPrice();
        this.totalPrice = product.getDiscountedPrice();
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getDiscountedPrice();
        this.totalPrice = (product.getDiscountedPrice())*quantity;
    }



    public CartItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void increaseQuantity() {
        this.quantity++;
        this.totalPrice = this.price* quantity;

    }

    public void decreaseQuantity() {
        this.quantity--;
        this.totalPrice = this.price* quantity;
    }



    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }


}
