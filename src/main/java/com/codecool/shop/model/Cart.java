package com.codecool.shop.model;

import com.codecool.shop.util.CartStatusType;

import java.util.ArrayList;
import java.util.List;

// TODO: DOES NOT USE ID, NAME AND DESCRIPTION FIELDS. calling getName() and getDescription() methods can cause problems
public class Cart extends BaseModel {

    private int userId = 1;
    private CartStatusType cartStatusType = CartStatusType.EMPTY;

    private List<CartItem> cartItemList = new ArrayList<>();


    public Cart() {
    }

    public Cart(int id, int userId, CartStatusType cartStatusType) {
        super(id);
        this.userId = userId;
        this.cartStatusType = cartStatusType;
    }

    public int getUserId() {
        return userId;
    }

    public CartStatusType getCartStatusType() {
        return cartStatusType;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCartStatusType(CartStatusType cartStatusType) {
        this.cartStatusType = cartStatusType;
    }

    public void addToCartItemList(CartItem cartItem) {
        cartItemList.add(cartItem);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartItemList=" + cartItemList +
                "}";
    }
}
