package com.example.CS308BackEnd2.service;


import com.example.CS308BackEnd2.model.*;
import com.example.CS308BackEnd2.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WishListService {

    private final ListItemRepository listItemRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;


    @Autowired
    public WishListService(ListItemRepository listItemRepository, ProductRepository productRepository,
                               WishListRepository wishListRepository, UserRepository userRepository) {
        this.listItemRepository = listItemRepository;
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
        this.userRepository = userRepository;
    }

    public void createInitialWishList(){
        wishListRepository.save(new WishList());
    }

    public Long createNewWishList(){
        WishList newWL = new WishList();
        wishListRepository.save(newWL);
        return newWL.getId();
    }

    public void setUserWishList(long userId, long wishListId){
        WishList wishList = wishListRepository.findById(wishListId).get();
        User user = userRepository.findById(userId).get();
        wishList.setUser(user);
        wishListRepository.save(wishList);
        //userRepository.save(user);
    }


    public String addListItemToWishList(long listItemId, long userId){
        User user = userRepository.findById(userId).get();
        WishList wishList = wishListRepository.findById(user.getWishList().getId()).get();
        ListItem listItem = listItemRepository.findById(listItemId).get();
        wishList.addListItem(listItem);
        wishListRepository.save(wishList);
        return "Item added to wish list";
    }

    public String removeListItemFromWishList(long listItemId, long userId){
        User user = userRepository.findById(userId).get();
        WishList wishList = wishListRepository.findById(user.getWishList().getId()).get();
        ListItem listItem = listItemRepository.findById(listItemId).get();
        wishList.removeListItem(listItem);
        wishListRepository.save(wishList);
        return "Item removed from wish list";
    }

    public void removeAllListItemsFromWishList(Long userId){
        User user = userRepository.findById(userId).get();
        WishList wishList = wishListRepository.findById(user.getWishList().getId()).get();
        wishList.clearListItems();
        wishListRepository.save(wishList);
    }
}
