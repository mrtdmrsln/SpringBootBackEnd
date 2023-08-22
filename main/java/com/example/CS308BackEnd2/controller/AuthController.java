package com.example.CS308BackEnd2.controller;


import com.example.CS308BackEnd2.config.JwtAuthenticationFilter;
import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.LoginDto;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.service.CartItemService;
import com.example.CS308BackEnd2.service.CustomUserServiceDetail;
import com.example.CS308BackEnd2.service.ShoppingCartService;
import com.example.CS308BackEnd2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, CartItemService cartItemService,
                          UserService userService, ShoppingCartService shoppingCartService, CustomUserServiceDetail customUserServiceDetail) {
        this.authenticationManager = authenticationManager;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }


    @PostMapping("/login")
    public ResponseEntity<String> AuthenticateUser(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

           

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
            jwtAuthenticationFilter.successfulAuthentication(request, response, null, authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> user = userService.getUserByEmail(loginDto.getEmail());
            User user1 = user.get();
            List<CartItem> cartItems = cartItemService.createByDTOList(loginDto.getItems());
            Long shoppingCartId = user1.getShoppingCart().getId();
            shoppingCartService.addCartItemsToShoppingCart(cartItems, shoppingCartId);
            Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
            String result = authentication1.getPrincipal().toString();

            //response.setHeader("Authorization", "Set-Cookie");
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Set-Cookie");

            return new ResponseEntity<>(response.getHeader("Set-Cookie"), HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>("BAD CREDENTIALS" + e, HttpStatus.BAD_REQUEST);
        }
    }

    //find a way to get the user from the token


    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(){
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logout Successful", HttpStatus.OK);
    }

    // find a way to get the user from the token
    @GetMapping("/user")
    public ResponseEntity<String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(authentication.getName());
        }
        return null;

    }



}
