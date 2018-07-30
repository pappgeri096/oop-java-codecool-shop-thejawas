package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.util.List;

public interface ProductDao extends BaseDAO<Product> {

    List<Product> getBy(Supplier supplier);

    List<Product> getBy(ProductCategory productCategory);

    Product getBy(int id);

}
