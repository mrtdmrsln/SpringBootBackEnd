package com.example.CS308BackEnd2.controller;



import com.example.CS308BackEnd2.model.Category;
import com.example.CS308BackEnd2.model.ChangePriceDTO;
import com.example.CS308BackEnd2.model.DisRateReqBodyDTO;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/product")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id){
        Product deletedProduct = productService.deleteProduct(id);
        return ResponseEntity.ok(deletedProduct);
    }

    @DeleteMapping("/delete/All/Caution")
    public ResponseEntity<String> deleteAllProducts(){
        String Message = productService.deleteAllProducts();
        return ResponseEntity.ok(Message);
    }

    /*
    @PostMapping("/map/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerUserCredentialThroughMap(@RequestBody Map<String, String> userMap){
        System.out.println("User ID: "+userMap.get("userName"));
        System.out.println("User ID: "+userMap.get("password"));
        return userMap;
    }
     */

    @PostMapping("/changePrice")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePriceOfProduct(@RequestBody ChangePriceDTO productMap){
        productService.changePrice(productMap.getProdID(), productMap.getNewPrice());
    }

    @PostMapping("/changeStock")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> changeStockOfProduct(@RequestBody Map<String, Object> productMap){
        Product product = productService.changeStock(((Integer) productMap.get("prodID")).longValue(), ((Integer) productMap.get("newStock")).intValue());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/changeDiscountRates")
    @ResponseBody
    public void changeDiscountRatesOfProducts(@RequestBody DisRateReqBodyDTO requestBody) throws JsonProcessingException {
        List<Double> discountRateList = requestBody.getDiscountRateList();
        List<Long> prodIDList = requestBody.getProdIDList();

        productService.changeDiscountRatesOfProds(prodIDList, discountRateList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> allCategories = productService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/search/{search1}")
    @ResponseBody
    public ResponseEntity<List<Product>> searchProducts(@PathVariable("search1") String search){
        List<Product> searchedProducts = productService.getSearchResults(search, search);
        return ResponseEntity.ok(searchedProducts);
    }

}

