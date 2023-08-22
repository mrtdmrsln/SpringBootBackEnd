package com.example.CS308BackEnd2.model;


import javax.persistence.*;
import java.net.URL;

//@Data
@Entity
@Table(name = "book")
@PrimaryKeyJoinColumn(name = "pID")
public class Book extends Product{

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "page")
    private int page;

    @Column(name = "language")
    private String language;

    @Column(name = "publicationDate")
    private String publicationDate;

    @Column(name = "edition")
    private int edition;

    public enum type {HARDCOPY, EBOOK, AUDIOBOOK};

    @Column(name = "format")
    private type type;

    //@ToString.Exclude
    //@EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "genreID", referencedColumnName = "id")
    private Category category;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public Book.type getType() {
        return type;
    }

    public void setType(Book.type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Book() {
    }


    public Book(String name, String description, double price, String author, String publisher, int page,
                String language, String publicationDate, int edition, Book.type type,
                URL image, long stock, double discountRate, Product.warranty warrantyStatus) {

        super(name, price, description, image, stock, discountRate, warrantyStatus);
        this.author = author;
        this.publisher = publisher;
        this.page = page;
        this.language = language;
        this.publicationDate = publicationDate;
        this.edition = edition;
        this.type = type;
        //this.category = category;
    }

    /*@Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", page=" + page +
                ", language='" + language + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", edition=" + edition +
                ", type=" + type +
                ", category=" + category +
                '}';
    }*/
}

