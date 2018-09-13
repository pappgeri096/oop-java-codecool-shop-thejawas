package com.codecool.shop.dao;

import java.util.List;

public interface BaseDAO<T> {

    void add(T objectType);
    T find(int id);
    void remove(int id);
    List<T> getAll();

}
