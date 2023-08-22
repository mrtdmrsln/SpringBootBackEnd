package com.example.CS308BackEnd2.controller;

import com.example.CS308BackEnd2.model.Invoice;
import com.example.CS308BackEnd2.service.EmailService;
import com.example.CS308BackEnd2.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/invoice")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {this.invoiceService = invoiceService;}

    @GetMapping("/getAll")
    public ResponseEntity<List<Invoice>> getAllInvoices(){
        List<Invoice> invoices = invoiceService.getInvoicesByTotalPriceIsNot(0);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/get/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable("invoiceId") long invoiceId){
        Invoice in = invoiceService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(in);
    }

    @GetMapping("/getByOrderId/{orderId}")
    public ResponseEntity<Invoice> getInvoiceByOrderId(@PathVariable("orderId") long orderId){
        Invoice in = invoiceService.getInvoiceByOrderId(orderId);
        return ResponseEntity.ok(in);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<Invoice>> getInvoiceByUserId(@PathVariable("userId") long userId){
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }



}
