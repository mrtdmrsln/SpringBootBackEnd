package com.example.CS308BackEnd2;

import com.example.CS308BackEnd2.model.Book;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Cs308BackEnd2Application implements CommandLineRunner{

	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewService reviewService;


	private final ShoppingCartService shoppingCartService;
	private final UserService userService;
	private final CartItemService cartItemService;

	@Autowired
	public Cs308BackEnd2Application(UserService userService, ShoppingCartService shoppingCartService,
									CartItemService cartItemService) {
		this.userService = userService;
		this.shoppingCartService = shoppingCartService;
		this.cartItemService = cartItemService;
	}

	public static void main(String[] args) {SpringApplication.run(Cs308BackEnd2Application.class, args);}


	@Override
	public void run(String... args) /*throws Exception*/ {

		/*productService.createBook("The Lord of the Rings", 100, 100, " copies sold.",
				null, 0, Product.warranty.YES, "J. R. R. Tolkien", "Allen & Unwin", "English", 1216, "29 July 1954", 1, Book.type.HARDCOPY);
		//productService.deleteBook(11);
		productService.createBook("The Lord of the Rings discounted", 100, 100, " copies sold.",
				null, 10, Product.warranty.YES, "J. R. R. Tolkien", "Allen & Unwin", "English", 1216, "29 July 1954", 1, Book.type.HARDCOPY);
		productService.createBook("The Lord of the Ringsssss", 100, 100, " copies sold.",
				null, 0, Product.warranty.YES, "J. R. R. Tolkien", "Allen & Unwin", "English", 1216, "29 July 1954", 1, Book.type.HARDCOPY);


		//reviewService.createReview(new Date(), 5, "This is a review");
		//reviewService.createReview(new Date(), 4, "This is a review");
		//reviewService.createReview(new Date(), 3, "This is a review");*/

		//shoppingCartService.createInitialShoppingCart();
		//shoppingCartService.setUserShoppingCart(1,1);
		/*cartItemService.createCartItem(1);

		shoppingCartService.addCartItemToShoppingCart(1,1);

		shoppingCartService.removeCartItemFromShoppingCart(1,1);*/



	}

	/*
	@Bean
	public void createInitialShoppingCart(){
		shoppingCartService.createInitialShoppingCart();
	}*/



}
