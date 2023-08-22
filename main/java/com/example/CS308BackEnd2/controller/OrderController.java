package com.example.CS308BackEnd2.controller;

import com.example.CS308BackEnd2.model.Order;
import com.example.CS308BackEnd2.service.InvoiceService;
import com.example.CS308BackEnd2.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/order")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;
    private final InvoiceService invoiceService;

    public OrderController(OrderService orderService, InvoiceService invoiceService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create/{userId}")
    public void createOrder(@PathVariable("userId") long userId){
        long orderId = orderService.createNewOrder(userId);
        invoiceService.createInvoice(orderId);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<Order>> getAllOrdersByUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok(orderService.getAllOrdersByUser(userId));
    }

    @PutMapping("/{orderId}/update")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.updateOrderStatus(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> updateOrderStatusWithName(@RequestBody Map<String, Object> orderMap){
        String result = orderService.updateOrderStatusWithName(((String) orderMap.get("statusName")).toString(),
                ((Integer) orderMap.get("orderId")).longValue());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable("orderId") long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Map<String,Order>> cancelOrder(@PathVariable("orderId") long orderId){
        Map<String,Order> result = orderService.cancelExistingOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateDateBef/{orderId}")
    public ResponseEntity<Order> updateOrderDateBefore30(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.setOrderDateBefore30(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/updateDateIn/{orderId}")
    public ResponseEntity<Order> updateOrderDateIn30(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.setOrderDateIn30(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/returnRequest/{orderId}")
    public ResponseEntity<Order> returnRequest(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.orderReturnRequest(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/returnRequestCancel/{orderId}")
    public ResponseEntity<Order> returnRequestCancel(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.orderReturnRequestRejected(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/returnRequestAccept/{orderId}")
    public ResponseEntity<Order> returnRequestAccept(@PathVariable("orderId") long orderId){
        Order updatedOrder = orderService.orderReturnRequestAccepted(orderId);
        return ResponseEntity.ok(updatedOrder);
    }


}
