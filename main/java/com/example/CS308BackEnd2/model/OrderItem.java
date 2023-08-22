package com.example.CS308BackEnd2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;

@Entity
@Table(name = "orderItemTable")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oiID")
    private Long id;

    @Column(name = "ProdID")
    private Long prodID;

    @Column(name = "ProdName")
    private String prodName;

    @Column(name = "imgUrl")
    private URL imgUrl;

    public enum Status{SOLD, RETURNED, CANCELLED}

    private Status status = Status.SOLD;



    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private double price;


  /*  @ManyToOne
    @JoinColumn(name = "ProdID", referencedColumnName = "Pid")
    private Product product;*/

    @ManyToOne
    @JoinColumn(name = "invoID", referencedColumnName = "invoiceId")
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "orderID", referencedColumnName = "oID")
    @JsonIgnore
    private Order order;

    public OrderItem() {
    }

    public OrderItem(Integer quantity, Order order, Product product) {
        this.quantity = quantity;
        //this.product = product;
        this.price = product.getDiscountedPrice();
        this.prodID = product.getId();
        this.prodName = product.getName();
        this.imgUrl = product.getImage();
    }



    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Long getProdID() {
        return prodID;
    }

    public void setProdID(Long prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public URL getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(URL imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /*
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }*/
}
