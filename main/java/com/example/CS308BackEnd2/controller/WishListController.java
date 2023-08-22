package com.example.CS308BackEnd2.controller;


import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.ListItem;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(maxAge = 3600,  allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class WishListController {

    private final ListItemService listItemService;
    private final WishListService wishListService;

    private final UserService userService;

    @Autowired
    public WishListController(ListItemService listItemService, WishListService wishListService,
                          UserService userService) {
        this.listItemService = listItemService;
        this.wishListService = wishListService;
        this.userService = userService;
    }

    @PostMapping("/add/{prodId}/{userId}")
    public ResponseEntity<String> addListItemToList(@PathVariable long prodId, @PathVariable long userId){
        ListItem L1 = listItemService.createListItem(prodId);
        String result = wishListService.addListItemToWishList(L1.getId(), userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/remove/{prodId}/{userId}")
    public void removeListItem(@PathVariable long prodId, @PathVariable long userId){
        User user = userService.getUserById(userId);
        ListItem listItem = user.getWishList().getListItems().stream()
                .filter(listItem1 -> listItem1.getProduct().getId() == prodId)
                .findFirst().get();
        wishListService.removeListItemFromWishList(listItem.getId(), userId);
    }

    // remove all the listitems
    @PostMapping("/removeAll/{userId}")
    public void removeAllListItems(@PathVariable long userId){
        wishListService.removeAllListItemsFromWishList(userId);
    }

    // get the listitems
    @GetMapping("/get/{userId}")
    public Iterable<ListItem> getListItems(@PathVariable long userId){
        User user = userService.getUserById(userId);
        return user.getWishList().getListItems();
    }
}
