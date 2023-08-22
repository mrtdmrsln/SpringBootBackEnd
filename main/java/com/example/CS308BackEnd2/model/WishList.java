package com.example.CS308BackEnd2.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wishList")
public class WishList {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "WLid")
    private long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "wishList")
    private Set<ListItem> listItems = new HashSet<>();

    @OneToOne
    private User user;

    public WishList() {
    }

    public WishList(long id, Set<ListItem> listItems, User user) {
        this.id = id;
        this.listItems = listItems;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(Set<ListItem> listItems) {
        this.listItems = listItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setWishList(this);
    }

    public void addListItem(ListItem listItem) {
        this.listItems.add(listItem);
        listItem.setWishList(this);
    }

    public void removeListItem(ListItem listItem) {
        this.listItems.remove(listItem);
        listItem.setWishList(null);
    }

    public void clearListItems() {
    	this.listItems.clear();
    }

    public boolean isInWishlist(Product product){
        for (ListItem item : this.listItems) {
            if (item.getProduct().getId() == product.getId()) {
                return true;
            }
        }
        return false;
    }


}
