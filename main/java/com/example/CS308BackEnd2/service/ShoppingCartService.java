package com.example.CS308BackEnd2.service;


import com.example.CS308BackEnd2.model.*;

import com.example.CS308BackEnd2.repository.CartItemRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;
import com.example.CS308BackEnd2.repository.ShoppingCartRepository;
import com.example.CS308BackEnd2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class ShoppingCartService {


    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;


    @Autowired
    public ShoppingCartService(CartItemRepository cartItemRepository, ProductRepository productRepository,
                               ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
    }


    public void createInitialShoppingCart(){
        shoppingCartRepository.save(new ShoppingCart());
    }

    public Long createNewShoppingCart(){
        ShoppingCart newSC = new ShoppingCart();
        shoppingCartRepository.save(newSC);
        return newSC.getId();
    }

    public void setUserShoppingCart(long userId, long shoppingCartId){
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).get();
        User user = userRepository.findById(userId).get();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        //userRepository.save(user);
    }


    public String addCartItemToShoppingCart(long cartItemId, long userId){
        User user = userRepository.findById(userId).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        String result = shoppingCart.addCartItem(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return result;
    }

    //not working rn
    public void removeListOfCartItemsFromShoppingCart(List<Long> cartItemIds, long userId){
        User user = userRepository.findById(userId).get();
        for (int i = 0; i < cartItemIds.size(); i++){
            ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
            CartItem cartItem = cartItemRepository.findById(cartItemIds.get(i)).get();
            shoppingCart.removeCartItemAllTogether(cartItem);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    public void removeCartItemFromShoppingCartEntirely(long cartItemId, long userId){
        User user = userRepository.findById(userId).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        shoppingCart.removeCartItemAllTogether(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    public void removeCartItemFromShoppingCart(long cartItemId, long userId){
        User user = userRepository.findById(userId).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        shoppingCart.removeCartItem(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    public void removeAllCartItems(long userId){
        User user = userRepository.findById(userId).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
        shoppingCart.clearCart();
        shoppingCartRepository.save(shoppingCart);
    }

    public void addCartItemsToShoppingCart(List<CartItem> items, long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).get();
        for(CartItem cartItem : items){
            shoppingCart.addCartItem(cartItem);
        }
        shoppingCartRepository.save(shoppingCart);

    }
}
