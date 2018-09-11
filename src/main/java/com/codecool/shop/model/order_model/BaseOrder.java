package com.codecool.shop.model.order_model;

import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class BaseOrder {

    int id;

    BaseOrder() {
    }

    BaseOrder(int id) {
        this.id = id;
    }

    public abstract int getId();
    public abstract void setId(int id);
    public abstract void addProduct(Product product);
    public abstract void createUserDataMap(List<String> userData);
    public abstract Map<String, String> getUserDataMap();
    public abstract List<LineItem> getLineItemList();
    public abstract void createProductNameAndQuantityMaps();
    public abstract Map<String, Integer> getProductNameAndQuantityMap();
    public abstract int getQuantityOfProducts();
    public abstract BigDecimal getTotalPrice();
}
