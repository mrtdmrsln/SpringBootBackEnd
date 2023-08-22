package com.example.CS308BackEnd2.service;

import com.example.CS308BackEnd2.model.*;
import com.example.CS308BackEnd2.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final EmailService emailService;

    @Autowired
    public OrderService(CartItemRepository cartItemRepository, ProductRepository productRepository,
                        ShoppingCartRepository shoppingCartRepository, UserRepository userRepository,
                        OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        EmailService emailService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Long createNewOrder(Long userId){

        Order order = new Order();
        ShoppingCart shoppingCart = userRepository.getById(userId).getShoppingCart();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for(CartItem cartItem : cartItems){
            Product product = cartItem.getProduct();
            OrderItem newOrderItem = new OrderItem(cartItem.getQuantity(), order, product);
            //product.addOrderItem(newOrderItem);

            orderItemRepository.save(newOrderItem);
            order.addOrderItem(newOrderItem);

            cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());
        }
        order.setTotalPrice(shoppingCart.getCartTotal());
        order.setUser(userRepository.getById(userId));
        orderRepository.save(order);
        shoppingCart.clearCart();
        shoppingCartRepository.save(shoppingCart);
        return order.getId();
    }

    public void deleteOrder(Long orderId){
        orderRepository.deleteById(orderId);
    }

    public Order updateOrderStatus(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        if (order.getStatus() == Order.Status.PROCESSING) {
            order.setStatus(Order.Status.IN_TRANSIT);
        } else if (order.getStatus() == Order.Status.IN_TRANSIT) {
            order.setStatus(Order.Status.DELIVERED);
        } else if (order.getStatus() == Order.Status.DELIVERED) {
            order.setStatus(Order.Status.RETURNED);
        } else if (order.getStatus() == Order.Status.RETURNED) {
            order.setStatus(Order.Status.CANCELLED);
        } else {
            order.setStatus(Order.Status.PROCESSING);
        }
        return orderRepository.save(order);
    }

    public String updateOrderStatusWithName(String statusName, Long orderId){

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        try {
            Order.Status stat = Order.Status.valueOf(statusName);
        }catch (Exception IllegalArgumentException){
            return "'statusName' does not correspond to any status values.";
        };
        order.setStatus(Order.Status.valueOf(statusName));
        orderRepository.save(order);

        return "Status changed to: " + order.getStatus();
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersByUser(Long userId){
        return orderRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    public Order getOrder(long orderId){
        return orderRepository.findById(orderId).get();
    }

    public List<Order> getAllOrdersByStatus(Order.Status status){
        return orderRepository.findAllByStatus(status);
    }

    public Map<String,Order> cancelExistingOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        Map<String, Order> map = new HashMap<String,Order>();
        if(order.getStatus() == Order.Status.PROCESSING){
            StringBuilder sb = new StringBuilder();
            order.setStatus(Order.Status.CANCELLED);
            for(OrderItem orderItem : order.getOrderItems()){
                Product product = productRepository.findById(orderItem.getProdID()).get();
                sb.append(product.getName() + "\n");
                product.setStock(product.getStock() + orderItem.getQuantity());
                orderItem.setStatus(OrderItem.Status.RETURNED);
                productRepository.save(product);
            }

            String body = "Your Order Return Request Accepted for the products: \n\n" + sb +
                    "\n\nTotal Price: " + String.format("%.2f",order.getTotalPrice()) + "$ is returned to your account.";

            emailService.sendEmailForReturnOrder(body, order.getUser().getEmail());

            Order updatedOrder = orderRepository.save(order);
            String result = "Order with id " + orderId + " has been cancelled.";
            map.put(result, updatedOrder);
            return map;
        }
        else{
            String result =  "Order with id " + orderId + " cannot be cancelled.";
            map.put(result, order);
            return map;
        }
    }

    public Order setOrderDateBefore30(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        Invoice invoice = order.getInvoice();
        Date date = new Date(122,Calendar.APRIL, 1, 0, 0, 0);//it is 1st of April 2022
        order.setCreatedDate(date);
        invoice.setCreatedDate(date);
        return orderRepository.save(order);

    }

    public Order setOrderDateIn30(long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        Invoice invoice = order.getInvoice();
        Date date = new Date(123,Calendar.JUNE, 5, 0, 0, 0);//it is 1st of March 2021
        order.setCreatedDate(date);
        invoice.setCreatedDate(date);
        return orderRepository.save(order);
    }

    public Order orderReturnRequest(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        order.setPreviousStatus(order.getStatus());
        order.setStatus(Order.Status.RETURN_REQUESTED);
        return orderRepository.save(order);
    }

    public Order orderReturnRequestAccepted(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        order.setStatus(Order.Status.RETURNED);

        StringBuilder productName = new StringBuilder();

        for(OrderItem orderItem : order.getOrderItems()){
            Product product = productRepository.findById(orderItem.getProdID()).get();
            product.setStock(product.getStock() + orderItem.getQuantity());
            productName.append(product.getName()).append("\n ");
            productRepository.save(product);
        }

        String email = order.getUser().getEmail();
        String body = "Your Order Return Request Accepted for the products: \n\n" + productName +
                "\n\nTotal Price: " + String.format("%.2f",order.getTotalPrice()) + "$ is returned to your account.";

        emailService.sendEmailForReturnOrder(body, email);



        return orderRepository.save(order);
    }

    public Order orderReturnRequestRejected(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist."));
        order.setStatus(order.getPreviousStatus());

        String email = order.getUser().getEmail();
        String body = "Your Order Return Request Rejected. \n\n Please contact with our customer service for more information. \n\n" +
                "CS308 Group 2.";

        emailService.sendEmailForReturnOrder(body, email);

        return orderRepository.save(order);
    }




}
