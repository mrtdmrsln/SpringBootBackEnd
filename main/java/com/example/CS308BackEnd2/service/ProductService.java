package com.example.CS308BackEnd2.service;



import com.example.CS308BackEnd2.model.*;
import com.example.CS308BackEnd2.repository.BookRepository;
import com.example.CS308BackEnd2.repository.CategoryRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.CS308BackEnd2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class ProductService {


    private final BookRepository bookRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ListItemService listItemService;
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public ProductService(BookRepository bookRepository, ProductRepository productRepository,
                          UserRepository userRepository, EmailService emailService, ListItemService listItemService,
                          CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.listItemService = listItemService;
        this.categoryRepository = categoryRepository;
    }

    public Product getLastProduct() {
        return productRepository.findFirstByOrderByIdDesc();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> getSearchResults(String search1, String search2) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search1, search2);
    }



    @Transactional
    public void createBook(String name, double price, int stock, String description, URL image,
                           double discountRate, Product.warranty warranty,
                           String author, String publisher, String language, int pageCount,
                           String publicationDate, int edition, Book.type type) {

        /*
        Book book = new Book();
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setLanguage(language);
        book.setPage(pageCount);
        book.setPublicationDate(publicationDate);
        book.setEdition(edition);
        book.setType(type);

        bookRepository.save(book);

        Product product;
        product = getLastProduct();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setDescription(description);
        product.setImage(image);
        product.setDiscountRate(discountRate);
        product.setWarrantyStatus(warranty);

        productRepository.save(product);*/

        Book book = new Book(name, description,price,author,publisher,pageCount,language,publicationDate,edition,
                type,image,stock,discountRate, warranty);
        bookRepository.save(book);
    }

    public Product addBook(BookDto bookDto){
        /*
        Book book1 = new Book();
        book1.setAuthor(book.getAuthor());
        book1.setPublisher(book.getPublisher());
        book1.setLanguage(book.getLanguage());
        book1.setPage(book.getPage());
        book1.setPublicationDate(book.getPublicationDate());
        book1.setEdition(book.getEdition());
        book1.setType(book.getType());

        bookRepository.save(book1);

        Product product1;
        product1 = getLastProduct();
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setStock(product.getStock());
        product1.setDescription(product.getDescription());
        product1.setImage(product.getImage());
        product1.setDiscountRate(product.getDiscountRate());
        product1.setWarrantyStatus(product.getWarrantyStatus());

        return productRepository.save(product1);*/
        Book book1 = new Book(bookDto.getName(), bookDto.getDescription(),bookDto.getPrice(),bookDto.getAuthor(),bookDto.getPublisher(),
                bookDto.getPage(), bookDto.getLanguage(),bookDto.getPublicationDate(),bookDto.getEdition(),bookDto.getType(),
                bookDto.getImage(),bookDto.getStock(), bookDto.getDiscountRate(), bookDto.getWarrantyStatus());

        Book book2 = bookRepository.save(book1);

        Product prod = getLastProduct();
        addCategoryToBook(prod.getId(), bookDto.getCategory());
        Product prod2 = getLastProduct();


        return prod2;


    }

    public List<Book> getDiscountedBooks() {
        return bookRepository.findBooksByDiscountRateNot(0);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findBooksByCategoryName(category);
    }

    public Product changeStock(long pid, int stockAmount){
        Product product = productRepository.findById(pid).get();
        product.setStock(stockAmount);
        productRepository.save(product);

        return product;
    }

    public Book addCategoryToBook(long id, String category) {
        Book book = bookRepository.findById(id).get();

        if(book.getCategory() == null){
            if (!categoryRepository.existsByNameIgnoreCase(category)){
                Category cat = new Category();
                cat.setName(category);
                cat.addBook(book);
                Category cat2 = categoryRepository.save(cat);

                //categoryRepository.save(cat2);

                //Category cat = categoryRepository.findByNameIs(cat.getName());
                //book.setCategory(cat2);
                bookRepository.save(book);

            }
            else{
                Category cat2 = categoryRepository.findByNameIsIgnoreCase(category);
                cat2.addBook(book);
                categoryRepository.save(cat2);
            }
        }
        else{
            throw new RuntimeException("Book already has a category");
        }

        return book;
        /*
        if (category.getBooks() == null || !(category.getBooks().contains(book))){
            category.addBook(book);
            Category cat = new Category();
            cat.setName(category.getName());
            if(book.getCategory().getName() != null){
                throw new RuntimeException("Book already has a category");
            }
            else{
                book.setCategory(cat);
                bookRepository.save(book);
                if (!categoryRepository.existsByName(category.getName())){
                    categoryRepository.save(cat);
                }
            }
        }
        return book;*/
    }

    public Book deleteCategoryFromBook(long id, Category category) {
        Book book = bookRepository.findById(id).get();
        Category cat = categoryRepository.findByNameIsIgnoreCase(category.getName());
        if (cat.getBooks() != null && cat.getBooks().contains(book)){
            cat.removeBook(book);
            book.setCategory(null);
            bookRepository.save(book);
            categoryRepository.save(cat);
        }
        return book;
    }

    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Product deleteProduct(long id) {
        Product product = productRepository.findById(id).get();
        productRepository.deleteById(id);
        return product;
    }

    public Product updateProduct(long id, Product product) {
        Product product1 = productRepository.findById(id).get();
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setStock(product.getStock());
        product1.setDescription(product.getDescription());
        product1.setImage(product.getImage());
        product1.setDiscountRate(product.getDiscountRate());
        product1.setWarrantyStatus(product.getWarrantyStatus());
        return productRepository.save(product1);
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }

    public String deleteAllProducts() {
        productRepository.deleteAll();
        return "What Have YOU DONE?? All products deleted";
    }

    public void changePrice(Long id, Double price){
        Product product = productRepository.findById(id).get();
        product.setPrice(price);
        product.setDiscountedPrice(price*(1-product.getDiscountRate()));
        productRepository.save(product);
    }
    /*
    "Dear " + invoiceService.getInvoiceById(invoiceId).getUser().getName()+ ",\n\n" + "You may find your" +
                    " invoice in the attachments" + "\n\n" +
                    "Thank you for choosing us!\n\n" +
                    "Best regards,\n" +
                    "CS308 Project Group 2"
     */

    public String generateEmailBody(String username, Product product, Double newDiscRate){

        Double newPrice = product.getPrice()*(1-newDiscRate);
        Double discPrice = product.getDiscountedPrice();

        String mailBody = "Dear " + username + ",\n\n" + "An item on your wishlist is on SALE!" + "\n\n" +
                product.getName() + "\n\n" +
                "Before:  " + String.format("%.2f", discPrice) + "$" + "\n"+
                "Now:  " + String.format( "%.2f", newPrice)+ "$" + "\n\n" +
                "Thank you for choosing us!\n\n" +
                "Best regards,\n" +
                "CS308 Project Group 2";

        return mailBody;
    }

    public void changeDiscountRatesOfProds(List<Long> prodIds, List<Double> DisRates){

        for(int i = 0; i < prodIds.size(); i++){
            Product product = productRepository.findById(prodIds.get(i)).get();
            List<User> allUsers = userRepository.findAll();

            for (User user : allUsers) {
                if (user.getWishList().isInWishlist(product)) {
                    String mailBody = generateEmailBody(user.getName(), product, DisRates.get(i));
                    try {
                        emailService.sendEmailWithPrompt(mailBody, user.getEmail());
                        log.info("Email is sent successfully.");
                    } catch (Exception e) {
                        log.info("Could not send the email.");
                    }
                }
            }

            product.setDiscountRate(DisRates.get(i));
            product.setDiscountedPrice(product.getPrice()* (1-product.getDiscountRate()));
            productRepository.save(product);
        }
    }

}

