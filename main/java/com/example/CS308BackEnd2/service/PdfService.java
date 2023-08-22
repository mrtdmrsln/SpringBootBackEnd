package com.example.CS308BackEnd2.service;

import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@Slf4j
public class PdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private InvoiceService invoiceService;
    private SpringTemplateEngine templateEngine;

    @Autowired
    public PdfService(InvoiceService invoiceService, SpringTemplateEngine templateEngine) {
        this.invoiceService = invoiceService;
        this.templateEngine = templateEngine;
    }

    private Context getContext(long invoiceId) {
        Context context = new Context();
        context.setVariable("invoice", invoiceService.getInvoiceById(invoiceId));
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("pdf_invoice", context);
    }

    private File renderPdf(String html) throws IOException, DocumentException {
        File file = File.createTempFile("invoice", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    public File generatePdf(long invoiceId) throws IOException, DocumentException { //create and return a pdf
        Context context = getContext(invoiceId);
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }






}
