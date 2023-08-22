package com.example.CS308BackEnd2.controller;


import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.ShoppingCart;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.service.CartItemService;
import com.example.CS308BackEnd2.service.ShoppingCartService;
import com.example.CS308BackEnd2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//More imports


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(maxAge = 3600,  allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class CartController {

    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    private final UserService userService;

    @Autowired
    public CartController(CartItemService cartItemService, ShoppingCartService shoppingCartService,
                          UserService userService) {
        this.cartItemService = cartItemService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PutMapping("/bind/{userId}/{cartId}")
    public void bindCartToUser(@PathVariable long userId, @PathVariable long cartId){
        shoppingCartService.setUserShoppingCart(userId, cartId);
    }

    @PostMapping("/add/{prodId}/{userId}")
    public ResponseEntity<String> addCartItemToCart(@PathVariable long prodId, @PathVariable long userId){
        CartItem c1 = cartItemService.createCartItem(prodId);
        String result = shoppingCartService.addCartItemToShoppingCart(c1.getId(), userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/remove/{prodId}/{userId}")
    public void removeCartItem(@PathVariable long prodId, @PathVariable long userId){
        User user = userService.getUserById(userId);
        CartItem cartItem = user.getShoppingCart().getCartItems().stream()
                .filter(cartItem1 -> cartItem1.getProduct().getId() == prodId)
                .findFirst().get();
        shoppingCartService.removeCartItemFromShoppingCart(cartItem.getId(), userId);
    }

    @PostMapping("/removeEntirely/{prodId}/{userId}")
    public void removeCartItemEntirely(@PathVariable long prodId, @PathVariable long userId){
        User user = userService.getUserById(userId);
        CartItem cartItem = user.getShoppingCart().getCartItems().stream()
                .filter(cartItem1 -> cartItem1.getProduct().getId() == prodId)
                .findFirst().get();
        shoppingCartService.removeCartItemFromShoppingCartEntirely(cartItem.getId(), userId);
    }

    @PostMapping("removeMulti/{userId}")
    @ResponseBody
    public void removeMultipleCartItems(@RequestBody List<Long> cartItems, @PathVariable long userId){
        //User user = userService.getUserById(userId);
        shoppingCartService.removeListOfCartItemsFromShoppingCart(cartItems, userId);
    }

    @PostMapping("/removeAll/{userId}")
    public void removeAllCartItems(@PathVariable long userId){
        shoppingCartService.removeAllCartItems(userId);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Set<CartItem>> getCartItems(@PathVariable long userId){
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user.getShoppingCart().getCartItems());
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createNewShoppingCart(){
        Long newSCid = shoppingCartService.createNewShoppingCart();
        return ResponseEntity.ok(newSCid);
    }

}
