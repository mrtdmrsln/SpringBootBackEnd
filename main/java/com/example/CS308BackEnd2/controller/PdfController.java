package com.example.CS308BackEnd2.controller;

import com.example.CS308BackEnd2.service.EmailService;
import com.example.CS308BackEnd2.service.InvoiceService;
import com.example.CS308BackEnd2.service.PdfService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/invoicePdf")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class PdfController {
    private final InvoiceService invoiceService;
    private final PdfService pdfService;
    private final EmailService emailService;

    @Autowired
    public PdfController(InvoiceService invoiceService, PdfService pdfService,
                         EmailService emailService) {
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    @GetMapping("/download-pdf/{invoiceId}")
    public ResponseEntity<String> downloadPDFResource(HttpServletResponse response, @PathVariable("invoiceId") long invoiceId) {
        try {
            Path file = Paths.get(pdfService.generatePdf(invoiceId).getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
                return ResponseEntity.ok(response.getHeader("Content-Disposition"));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    //not working just a dummy to solve a problem
    @GetMapping("/download-pdf2/{invoiceId}")
    public void downloadPDFResource2(HttpServletResponse response, @PathVariable("invoiceId") long invoiceId) {
        try {
            Path file = Paths.get(pdfService.generatePdf(invoiceId).getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=" + file.getFileName());
                //response.addHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void streamReport(HttpServletResponse response, byte[] data, String name)
            throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=" + name);
        response.setContentLength(data.length);

        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @PostMapping("/send-email/{invoiceId}")
    public String sendEmail(@PathVariable("invoiceId") long invoiceId) {
        try {
            emailService.sendEmailWithAttachment(invoiceId);
            return "Email sent successfully";
        } catch (Exception e) {
            return "Email could not be sent";
        }
    }

}
