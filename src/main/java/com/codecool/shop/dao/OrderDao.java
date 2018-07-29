package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

public interface OrderDao extends BaseDAO<Order> {

    Order getCurrent();
}
