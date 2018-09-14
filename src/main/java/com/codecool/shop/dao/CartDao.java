package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

import java.math.BigDecimal;
import java.util.Map;


public interface CartDao extends BaseDAO<Cart> {

    Cart getCurrent();
    BigDecimal getTotalPriceOfCurrentCart();

    void createProductNameAndQuantityMaps();
    Map<String, Integer> getProductNameAndQuantityMap();

    void clearProductNameAndQuantityMap();
}
