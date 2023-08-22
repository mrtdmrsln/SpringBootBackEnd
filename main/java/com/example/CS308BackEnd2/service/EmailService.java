package com.example.CS308BackEnd2.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final PdfService pdfService;
    private final InvoiceService invoiceService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine,
                        PdfService pdfService, InvoiceService invoiceService) {
        this.emailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.pdfService = pdfService;
        this.invoiceService = invoiceService;
    }

    public void sendEmailWithAttachment(long invoiceId) {
        try {
            Properties pro = new Properties();
            pro.put("mail.smtp.host", "smtp.gmail.com");
            pro.put("mail.smtp.starttls.enable", "true");
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.port", "587");
            // Create the email message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cs308projectg2@gmail.com");
            String toEmail = invoiceService.getInvoiceById(invoiceId).getUser().getEmail();
            helper.setTo(toEmail);
            helper.setSubject("Invoice for your order from CS308 Project Group 2");
            helper.setText("Dear " + invoiceService.getInvoiceById(invoiceId).getUser().getName()+ ",\n\n" + "You may find your" +
                    " invoice in the attachments" + "\n\n" +
                    "Thank you for choosing us!\n\n" +
                    "Best regards,\n" +
                    "CS308 Project Group 2");

            // Create the attachment
            File invoice = pdfService.generatePdf(invoiceId);
            FileSystemResource file = new FileSystemResource(invoice);
            helper.addAttachment("Invoice.pdf", file);

            // Send the email
            emailSender.send(message);

            log.info("Email sent to " + toEmail);
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }
    }

    public void sendEmailWithPrompt(String mailBody, String email) {

        try {
            Properties pro = new Properties();
            pro.put("mail.smtp.host", "smtp.gmail.com");
            pro.put("mail.smtp.starttls.enable", "true");
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.port", "587");
            // Create the email message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cs308projectg2@gmail.com");
            String toEmail = email;
            helper.setTo(toEmail);
            helper.setSubject("Products on your wishlist are on SALE! | CS308 Project Group 2");
            helper.setText(mailBody);

            // Send the email
            emailSender.send(message);

            log.info("Email sent to " + toEmail);
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }

    }

    public void sendEmailForRefund(String mailBody, String email, String ProductName) {

        try {
            Properties pro = new Properties();
            pro.put("mail.smtp.host", "smtp.gmail.com");
            pro.put("mail.smtp.starttls.enable", "true");
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.port", "587");
            // Create the email message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cs308projectg2@gmail.com");
            String toEmail = email;
            helper.setTo(toEmail);
            helper.setSubject("Result of your refund request for " + ProductName+ " | CS308 Project Group 2");
            helper.setText(mailBody);

            // Send the email
            emailSender.send(message);

            log.info("Email sent to " + toEmail);
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }

    }

    public void sendEmailForReturnOrder(String mailBody, String email) {

        try {
            Properties pro = new Properties();
            pro.put("mail.smtp.host", "smtp.gmail.com");
            pro.put("mail.smtp.starttls.enable", "true");
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.port", "587");
            // Create the email message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cs308projectg2@gmail.com");
            String toEmail = email;
            helper.setTo(toEmail);
            helper.setSubject("Your Order Return Assessment | CS308 Project Group 2");
            helper.setText(mailBody);

            // Send the email
            emailSender.send(message);

            log.info("Email sent to " + toEmail);
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }

    }
}
