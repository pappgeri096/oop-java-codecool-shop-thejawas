package com.codecool.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Supplier extends BaseModel {
//    @JsonIgnore
//    private List<Product> products;


    public Supplier() {
    }

    public Supplier(int id, String name, String description) {
        super(id, name, description);
//        this.products = new ArrayList<>();
    }

//    public void setProducts(ArrayList<Product> products) {
//        this.products = products;
//    }

//    public List<Product> getProducts() {
//        return this.products;
//    }
//
//    void addProduct(Product product) {
//        this.products.add(product);
//    }

    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }
}