package com.example.CS308BackEnd2.service;


import com.example.CS308BackEnd2.model.CartItem;
import com.example.CS308BackEnd2.model.ListItem;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.repository.ListItemRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListItemService {

    private final ListItemRepository listItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ListItemService(ListItemRepository listItemRepository, ProductRepository productRepository) {
        this.listItemRepository = listItemRepository;
        this.productRepository = productRepository;
    }

    public ListItem createListItem(long productId){
        if (productRepository.existsById(productId)){
            Product product = productRepository.findById(productId).get();
            ListItem listItem = new ListItem(product);
            product.addListItem(listItem);  //new
            ListItem L1 = listItemRepository.save(listItem);
            return L1;
        }
        else{
            throw new IllegalStateException("Product with id " + productId + " does not exist");
        }
    }
}
