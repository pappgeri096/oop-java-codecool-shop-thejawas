package com.codecool.shop.model;

import java.util.*;

public class Order {
    private int id;
    Map<String, String> map = new HashMap<String, String>();
    private List<String> checkoutData = Arrays.asList("fullName","emailAddress"," telephoneNumber","countryBill","cityBill","zipCodeBill","addressBill","countryShip","cityShip","zipCodeShip","addressShip");

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

    public void setUserData(List<String> userData) {
        for (int i = 0; i < 11; i++) {
            this.map.put(checkoutData.get(i), userData.get(i));
        }
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
