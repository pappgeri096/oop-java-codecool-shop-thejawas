package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.Map;


public interface CartDao extends BaseDAO<Cart> {

    Cart getLastCart();
    BigDecimal getTotalPriceOfLastCart();
    void addToCartItemList(Product product);
    int getQuantityOfProducts();

    void createProductNameAndQuantityMaps(); // TODO: REMOVE THESE
    Map<String, Integer> getProductNameAndQuantityMap(); // TODO: REMOVE THESE

    void clearProductNameAndQuantityMap(); // TODO: REMOVE THESE
}
