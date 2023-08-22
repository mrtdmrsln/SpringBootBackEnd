package com.example.CS308BackEnd2.service;

import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.CartItemDTO;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.repository.CartItemRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    public CartItem createCartItem(long productId){
        if (productRepository.existsById(productId)){
            Product product = productRepository.findById(productId).get();
            CartItem cartItem = new CartItem(product);
            product.addCartItem(cartItem);  //new
            CartItem c1 = cartItemRepository.save(cartItem);
            return c1;
        }
        else{
            throw new IllegalStateException("Product with id " + productId + " does not exist");
        }
    }

    public List<CartItem> createByDTOList(List<CartItemDTO> FeScItems){
        List<CartItem> ScItems = new ArrayList<>();
        for (CartItemDTO item : FeScItems){
            Product p1 = productRepository.findById(item.getId()).get();
            CartItem ScItem = new CartItem(p1, item.getQuantity());
            cartItemRepository.save(ScItem);
            ScItems.add(ScItem);
        }
        return ScItems;
    }

}
