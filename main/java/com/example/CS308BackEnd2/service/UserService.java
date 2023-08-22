package com.example.CS308BackEnd2.service;


import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.CartItemDTO;
import com.example.CS308BackEnd2.model.User;
import com.example.CS308BackEnd2.model.UserDTO;
import com.example.CS308BackEnd2.repository.WishListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.CS308BackEnd2.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final WishListService wishListService;

    @Autowired
    public UserService(UserRepository userRepository, ShoppingCartService shoppingCartService,
                       CartItemService cartItemService, WishListService wishListService) {
        this.userRepository = userRepository;
        this.shoppingCartService = shoppingCartService;
        this.cartItemService = cartItemService;
        this.wishListService = wishListService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User deleteUserById(long id) {
        return userRepository.deleteById(id);
    }

    public User deleteUserByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }

    public User createUser(User user){

        if (user.getRole()==null){
            user.setRole(User.role.ROLE_CUSTOMER);
        }

        User user1 = new User(user.getName(), user.getAge(),user.getEmail(), new BCryptPasswordEncoder().encode(user.getPassword()),
                user.getAddress(), user.getPhone(), user.getRole(), user.getGender());

        long shoppingCartId = shoppingCartService.createNewShoppingCart();
        long wishListId = wishListService.createNewWishList();
        User u1 = userRepository.save(user1);
        shoppingCartService.setUserShoppingCart(u1.getId(), shoppingCartId);
        wishListService.setUserWishList(u1.getId(), wishListId);

        //List<CartItem> cartItems = cartItemService.createByDTOList(items);

        //shoppingCartService.addCartItemsToShoppingCart(cartItems, shoppingCartId);

        return u1;
    }

    public User createUser(UserDTO user){

        if (user.getRole()==null){
            user.setRole(User.role.ROLE_CUSTOMER);
        }

        User user1 = new User(user.getName(), user.getAge(),user.getEmail(), new BCryptPasswordEncoder().encode(user.getPassword()),
                user.getAddress(), user.getPhone(), user.getRole(), user.getGender());

        long shoppingCartId = shoppingCartService.createNewShoppingCart();

        long wishListId = wishListService.createNewWishList();

        User u1 = userRepository.save(user1);

        u1.setTaxID();

        shoppingCartService.setUserShoppingCart(u1.getId(), shoppingCartId);
        wishListService.setUserWishList(u1.getId(), wishListId);

        List<CartItem> cartItems = cartItemService.createByDTOList(user.getItems());

        shoppingCartService.addCartItemsToShoppingCart(cartItems, shoppingCartId);

        return u1;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User deleteUser(long id) {
        return userRepository.deleteById(id);
    }


}
