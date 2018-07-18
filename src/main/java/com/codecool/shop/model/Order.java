package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private List<Product> products;

    public Order() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product != null) products.add(product);
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

    public List<Product> getProductList() {
        return products;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, ",
                this.id);

    }
}
