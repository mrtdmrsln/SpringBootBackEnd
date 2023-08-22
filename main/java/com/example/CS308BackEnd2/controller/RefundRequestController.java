package com.example.CS308BackEnd2.controller;


import com.example.CS308BackEnd2.model.RefundRequest;
import com.example.CS308BackEnd2.service.RefundRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ref-request")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class RefundRequestController {

    private final RefundRequestService refundRequestService;

    @Autowired
    public RefundRequestController(RefundRequestService refundRequestService) {
        this.refundRequestService = refundRequestService;
    }

    @PostMapping("/create/{OrderID}/{ProductID}")
    @ResponseBody
    public ResponseEntity<RefundRequest> createRefundRequest(@PathVariable long OrderID, @PathVariable long ProductID){
        RefundRequest request = refundRequestService.createRefundRequest(OrderID, ProductID);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/admin/all")
    @ResponseBody
    public ResponseEntity<?> getAllRefundRequests(){
        return ResponseEntity.ok(refundRequestService.getAllRefundRequests());
    }

    @PostMapping("/admin/approve/{id}")
    @ResponseBody
    public ResponseEntity<RefundRequest> acceptRefundRequest(@PathVariable long id){
        return ResponseEntity.ok(refundRequestService.acceptRefReq(id));
    }

    @PostMapping("/admin/reject/{id}")
    @ResponseBody
    public ResponseEntity<RefundRequest> rejectRefundRequest(@PathVariable long id){
        return ResponseEntity.ok(refundRequestService.rejectRefundRequest(id));
    }




}
