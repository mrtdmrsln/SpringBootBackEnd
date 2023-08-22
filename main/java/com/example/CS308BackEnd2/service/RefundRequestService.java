package com.example.CS308BackEnd2.service;


import com.example.CS308BackEnd2.model.*;
import com.example.CS308BackEnd2.repository.*;
import com.example.CS308BackEnd2.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RefundRequestService {

    private final RefundRequestRepository refundRequestRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final InvoiceRepository invoiceRepository;

    private final ProductRepository productRepository;

    private final EmailService emailService;

    @Autowired
    public RefundRequestService(RefundRequestRepository refundRequestRepository, OrderRepository orderRepository,
                                UserRepository userRepository, InvoiceRepository invoiceRepository,
                                ProductRepository productRepository, EmailService emailService) {
        this.refundRequestRepository = refundRequestRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    public RefundRequest createRefundRequest(long OrderID, long ProductID){
        Order order = orderRepository.findById(OrderID).get();
        Product product = productRepository.findById(ProductID).get();
        // check if the order is purchased in 30 days
        if(Utils.daysBetween(order.getCreatedDate(), new Date()) >= 30){
            throw new IllegalStateException("Order with id " + OrderID + " is purchased more than 30 days ago");
        }

        User user = orderRepository.findById(OrderID).get().getUser();
        RefundRequest refundRequest = new RefundRequest(OrderID, ProductID, product.getName());
        user.addRefundRequest(refundRequest);
        //userRepository.save(user);
        return refundRequestRepository.save(refundRequest);
    }

    public List<RefundRequest> getAllRefundRequests(){
        return refundRequestRepository.findAll();
    }


    public RefundRequest approveRefundRequest(long id) {
        RefundRequest refundRequest = refundRequestRepository.findById(id).get();
        refundRequest.setStatus(RefundRequest.Status.APPROVED);
        refundRequestRepository.save(refundRequest);

        long orderID = refundRequest.getOrderID();
        long productID = refundRequest.getProductID();
        User user = orderRepository.findById(orderID).get().getUser();
        Invoice invoice = invoiceRepository.findByOrderId(orderID);
        Order order = orderRepository.findById(orderID).get();
        Product product = productRepository.findById(productID).get();

        // remove the product from the order
        OrderItem item = order.removeItemByProductId(productID);
        order.setTotalPrice(order.getTotalPrice() - (item.getQuantity() * item.getPrice()));
        product.setStock(product.getStock() + item.getQuantity());
        productRepository.save(product);


        // remove the product from the invoice
        invoice.removeItemByProductId(productID);
        invoice.setTotalPrice(invoice.getTotalPrice() - (item.getQuantity() * item.getPrice()));

        if(order.getOrderItems().size() == 0){
            order.setStatus(Order.Status.RETURNED);
            orderRepository.save(order);
        }



        sendEmail(id,item.getPrice()*item.getQuantity(),user,product);

        return refundRequest;
    }

    public RefundRequest rejectRefundRequest(long id) {
        RefundRequest refundRequest = refundRequestRepository.findById(id).get();
        refundRequest.setStatus(RefundRequest.Status.REJECTED);
        refundRequestRepository.save(refundRequest);

        Order order = orderRepository.findById(refundRequest.getOrderID()).get();
        User user = order.getUser();
        Product product = productRepository.findById(refundRequest.getProductID()).get();

        sendEmail(id,0,user,product);
        return refundRequest;
    }

    public RefundRequest acceptRefReq(long id){
        RefundRequest refundRequest = refundRequestRepository.findById(id).get();
        refundRequest.setStatus(RefundRequest.Status.APPROVED);
        refundRequestRepository.save(refundRequest);

        long orderID = refundRequest.getOrderID();
        long productID = refundRequest.getProductID();
        User user = orderRepository.findById(orderID).get().getUser();
        Invoice invoice = invoiceRepository.findByOrderId(orderID);
        Order order = orderRepository.findById(orderID).get();
        Product product = productRepository.findById(productID).get();

        OrderItem item = order.returnedItemByProductId(productID);
        order.setTotalPrice(order.getTotalPrice() - (item.getQuantity() * item.getPrice()));
        product.setStock(product.getStock() + item.getQuantity());
        productRepository.save(product);

        invoice.returnedItemByProductId(productID);
        invoice.setTotalPrice(invoice.getTotalPrice() - (item.getQuantity() * item.getPrice()));

        if(order.isAllReturned()){
            order.setStatus(Order.Status.RETURNED);
            orderRepository.save(order);
        }

        sendEmail(id,item.getPrice()*item.getQuantity(),user,product);

        return refundRequest;
    }

    private void sendEmail(long id,double price,User user,Product product){
        RefundRequest refundRequest = refundRequestRepository.findById(id).get();
        String text;
        Order order = orderRepository.findById(refundRequest.getOrderID()).get();
        if (refundRequest.getStatus() == RefundRequest.Status.APPROVED) {
            text = "Your refund request has been approved. " + String.format("%.2f",price) + "$ is refunded to your account.";
        } else if (refundRequest.getStatus() == RefundRequest.Status.REJECTED) {
            text = "Your refund request has been rejected.";
        } else {
            text = "Your refund request is pending.";
        }
        String email = user.getEmail();

        String productName = product.getName();

        emailService.sendEmailForRefund(text, email, productName);
    }
}
