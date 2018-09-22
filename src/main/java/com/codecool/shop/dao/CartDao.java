package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;


public interface CartDao extends BaseDAO<Cart> {

    Cart getLastCart();
    BigDecimal getTotalPriceOfLastCart();
    void addToCartItemList(Product product);
    int getQuantityOfProducts();
}
