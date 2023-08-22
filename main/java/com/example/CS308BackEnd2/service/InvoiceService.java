package com.example.CS308BackEnd2.service;

import com.example.CS308BackEnd2.model.Invoice;
import com.example.CS308BackEnd2.model.Order;
import com.example.CS308BackEnd2.model.OrderItem;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.repository.InvoiceRepository;
import com.example.CS308BackEnd2.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
    }

    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public long createInvoice(long orderId){
        Invoice invoice = new Invoice();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));
        User user = order.getUser();
        invoice.setUser(user);
        invoice.setOrder(order);
        invoice.setAddress(user.getAddress());
        invoice.setEmail(user.getEmail());
        invoice.setPhoneNumber(user.getPhone());
        invoice.setCreatedDate(order.getCreatedDate());
        //invoice.setItems(order.getOrderItems());
        invoice.setTotalPrice(order.getTotalPrice());

        for(OrderItem orderItem : order.getOrderItems()){
            invoice.addItem(orderItem);
        }

        Invoice I1 = invoiceRepository.save(invoice);
        return I1.getInvoiceId();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new IllegalStateException("Invoice with id " + id + " does not exist"));
    }

    public void deleteInvoiceById(Long id) {
        invoiceRepository.deleteById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByUserId(Long id) {
        return invoiceRepository.findByUserId(id);
    }


    public Invoice getInvoiceByOrderId(long orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }

    public List<Invoice> getInvoicesByTotalPriceIsNot(double price) {
        return invoiceRepository.findByTotalPriceIsNot(price);
    }
}
