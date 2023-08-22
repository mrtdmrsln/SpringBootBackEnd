package com.example.CS308BackEnd2.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orderTable")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oID")
    private Long id;

    @Column(name = "creation_date")
    private Date createdDate;

    public enum Status {
        PROCESSING("processing"),
        IN_TRANSIT("in_transit"),
        DELIVERED("delivered"),
        RETURN_REQUESTED("return_requested"),
        RETURN_DENIED("request_denied"),
        RETURNED("returned"),
        CANCELLED("cancelled");

        Status(String status) {
        }
    };
    @Column(name = "status")
    private Order.Status status = Order.Status.PROCESSING;

    @Column(name = "total_price")
    private Double totalPrice;

    private Status previousStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "uID", referencedColumnName = "uID")
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Invoice invoice;

    public Order() {
        createdDate = new Date();
        orderItems = new ArrayList<OrderItem>();
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem){
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    public OrderItem removeItemByProductId(Long productId){
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProdID() == productId){
                orderItems.remove(orderItem);
                orderItem.setOrder(null);
                return orderItem;
            }
        }
        return null;
    }

    public OrderItem returnedItemByProductId(Long productId){
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProdID() == productId){
                orderItem.setStatus(OrderItem.Status.RETURNED);
                return orderItem;
            }
        }
        return null;
    }

    public boolean isAllReturned(){
        for(OrderItem orderItem : orderItems){
            if(orderItem.getStatus() != OrderItem.Status.RETURNED){
                return false;
            }
        }
        return true;
    }

    public OrderItem getOrderItemByProductId(Long productId){
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProdID() == productId){
                return orderItem;
            }
        }
        return null;
    }

    public void calculateTotalPrice(){
        double total = 0;
        for(OrderItem orderItem : orderItems){
            total += (orderItem.getPrice() * orderItem.getQuantity());
        }
        this.totalPrice = total;
    }



    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Order(List<OrderItem> orderItems, User user) {
        this.orderItems = orderItems;
        this.user = user;
        this.createdDate = new Date();
        this.status = Status.PROCESSING;

    }

    public Order(Date createdDate, Status status, Double totalPrice, List<OrderItem> orderItems, User user) {
        this.createdDate = createdDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPreviousStatus(Status previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Status getPreviousStatus() {
        return previousStatus;
    }
}
