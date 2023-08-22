package com.example.CS308BackEnd2.controller;




import com.example.CS308BackEnd2.model.Book;
import com.example.CS308BackEnd2.model.BookDto;
import com.example.CS308BackEnd2.model.Category;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/book")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class BookController {

    private final ProductService productService;

    @Autowired
    public BookController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createBook(@RequestBody BookDto bookDto) throws JsonProcessingException {
        Product createdBook = productService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/discount")
    public ResponseEntity<List<Book> > getDiscountedBooks(){
        List<Book> discountedBooks = productService.getDiscountedBooks();
        return ResponseEntity.ok(discountedBooks);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book> > getAllBooks(){
        List<Book> allBooks = productService.getAllBooks();
        return ResponseEntity.ok(allBooks);
    }



    @GetMapping("/category/{category}")
    public ResponseEntity<List<Book> > getBooksByCategory(@PathVariable("category") String category){
        List<Book> booksByCategory = productService.getBooksByCategory(category);
        return ResponseEntity.ok(booksByCategory);
    }

    @DeleteMapping("/category/{category_id}")
    public ResponseEntity deleteCategory(@PathVariable("category_id") long category_id){
        productService.deleteCategory(category_id);
        return ResponseEntity.ok().build();
    }

    // add a category to a book
    @PostMapping("/{id}/category")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> addCategoryToBook(@PathVariable("id") long id, @RequestBody String category){
        Book book = productService.addCategoryToBook(id, category);
        return ResponseEntity.ok(book);
    }

    // remove a category from a book
    @DeleteMapping("/{id}/category")
    public ResponseEntity<Book> removeCategoryFromBook(@PathVariable("id") long id, @RequestBody Category category){
        Book book = productService.deleteCategoryFromBook(id, category);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") long id){
        productService.deleteBook(id);
        return ResponseEntity.ok().build();
    }




}

