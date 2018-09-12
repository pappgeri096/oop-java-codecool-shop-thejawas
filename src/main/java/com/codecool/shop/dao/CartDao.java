package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

import java.math.BigDecimal;


public interface CartDao extends BaseDAO<Cart> {

    Cart getCurrent();

    BigDecimal getTotalPrice();
}
