package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private List<Product> products;

    public Order() {
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getProductQuantity(int id) {
        return products.stream().filter(t -> t.getId() == id).count();
    } // may be incorrect

    public int getId() {
        return this.id;
    }
}
