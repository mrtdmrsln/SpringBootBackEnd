package com.example.CS308BackEnd2.model;




import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.out;

@Entity
@Table(name = "shoppingCart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCid")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    private Set<CartItem> cartItems = new HashSet<>();

    @OneToOne
    private User user;

    public ShoppingCart(Long id, Set<CartItem> cartItems, User user) {
        this.id = id;
        this.cartItems = cartItems;
        this.user = user;
    }

    public ShoppingCart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setShoppingCart(this);
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String addCartItem(CartItem cartItem) {
        long productId = cartItem.getProduct().getId();
        if(cartItem.getProduct().getStock() - cartItem.getQuantity() < 0){
            out.println("No more stock");
            return "No more stock";
        }
        for (CartItem item : this.cartItems) {
            if (item.getProduct().getId() == productId) {
                //item.setQuantity(item.getQuantity() + 1);
                item.increaseQuantity();
                return "Added to cart";
            }
        }
        this.cartItems.add(cartItem);
        cartItem.setShoppingCart(this);
        return "Added to cart";
    }

    public void removeCartItem(CartItem cartItem) {
        if(this.cartItems.contains(cartItem)){
            for (CartItem item : this.cartItems) {
                if (item.getProduct().getId() == cartItem.getProduct().getId()) {
                    if(item.getQuantity() > 1){
                        //item.setQuantity(item.getQuantity() - 1);
                        item.decreaseQuantity();
                        return;
                    }
                }
            }
        }
        this.cartItems.remove(cartItem);
        cartItem.setShoppingCart(null);
    }

    public void removeCartItemAllTogether(CartItem cartItem) {
        if(this.cartItems.contains(cartItem)){
            for (CartItem item : this.cartItems) {
                if (item.getProduct().getId() == cartItem.getProduct().getId()) {
                    if(item.getQuantity() > 1){
                        //item.setQuantity(item.getQuantity() - 1);
                        this.cartItems.remove(item);
                        cartItem.setShoppingCart(null);
                        return;
                    }
                }
            }
        }
        this.cartItems.remove(cartItem);
        cartItem.setShoppingCart(null);
    }

    public void clearCart() {
        if (!this.cartItems.isEmpty()){
            for (CartItem item : this.cartItems) {
                item.setShoppingCart(null);
            }
            this.cartItems.clear();
        }
        this.cartItems.clear();
    }

    public int getCartSize() {
        return this.cartItems.size();
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem cartItem : this.cartItems) {
            total += cartItem.getTotalPrice();
        }
        return total;
    }






}
