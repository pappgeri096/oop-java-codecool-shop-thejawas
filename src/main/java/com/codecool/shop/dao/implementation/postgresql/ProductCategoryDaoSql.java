package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.List;

public class ProductCategoryDaoSql implements ProductDao {

    private static ProductCategoryDaoSql instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoSql() {
    }

    public static ProductCategoryDaoSql getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoSql();
        }
        return instance;
    }


    @Override
    public void add(Product product) {

    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }

    @Override
    public Product getBy(int id) {
        return null;
    }

    ProductCategory getProductCategoryById() {
        return null;
    }
}
