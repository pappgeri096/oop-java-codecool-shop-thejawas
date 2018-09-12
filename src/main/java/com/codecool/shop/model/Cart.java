package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

// TODO: DOES NOT ID USE NAME AND DESCRIPTION FIELDS. calling getName() and getDescription() methods can cause problems
public class Cart extends BaseModel {

    private List<CartItem> cartItemList = new ArrayList<>();

    public void addProduct(Product product) {
        boolean wasProductFound = false;
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getProduct().getId() == (product.getId())) {
                cartItem.increaseQuantity();
                wasProductFound = true;
            }
        }
        if (!wasProductFound){
            cartItemList.add(new CartItem(createIdForLineItem(), product));
        }

    }

    private int createIdForLineItem() {
        return cartItemList.size();
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public int getQuantityOfProducts() {
        int numberOfItems = 0;
        for (CartItem cartItem : cartItemList) {
            numberOfItems += cartItem.getQuantity();
        }
        return numberOfItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartItemList=" + cartItemList +
                "}";
    }
}
