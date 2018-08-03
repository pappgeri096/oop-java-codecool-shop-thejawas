package com.codecool.shop.dao;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T> {

    void add(T objectType);

    T find(int id);
    
    void remove(int id);

    List<T> getAll();

}
