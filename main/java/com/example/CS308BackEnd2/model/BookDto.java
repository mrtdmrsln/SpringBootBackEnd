package com.example.CS308BackEnd2.model;

import lombok.Data;

import java.net.URL;

@Data
public class BookDto {
    private String name;

    private double price;

    private String description;

    private URL image;

    private long stock;

    private double discountRate;

    private String author;

    private String publisher;

    private int page;

    private String language;

    private String publicationDate;

    private int edition;

    private Book.type type;

    private Product.warranty warrantyStatus;

    private String category;

}
