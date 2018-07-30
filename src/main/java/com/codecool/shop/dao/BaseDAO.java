package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

public interface BaseDAO<T> {

    void add(T order);
    T find(int id);
    void remove(int id);

    List<T> getAll();

}
