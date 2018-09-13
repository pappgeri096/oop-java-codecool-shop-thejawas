package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface CartDao extends BaseDAO<Cart> {

    Cart getCurrent();
    BigDecimal getTotalPrice();

//    void createUserDataMap(List<String> userData);
//    Map<String, String> getUserDataMap();
    void createProductNameAndQuantityMaps();
    Map<String, Integer> getProductNameAndQuantityMap();
}
