package com.codecool.shop.model;

import java.util.*;

public class Order {
    private int id;
    Map<String, String> map = new HashMap<>();
    private List<String> checkoutData = Arrays.asList("fullName","emailAddress"," telephoneNumber","countryBill","cityBill","zipCodeBill","addressBill","countryShip","cityShip","zipCodeShip","addressShip");

    private List<LineItem> lineItemList;

    public Order() {
        lineItemList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        boolean wasProductFound = false;
        for (LineItem lineItem: lineItemList) {
            if (lineItem.getProduct().getId()==(product.getId())){
                lineItem.increaseQuantity();
                wasProductFound=true;
            }
        }
        if (!wasProductFound) lineItemList.add(new LineItem(product));

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserData(List<String> userData) {
        for (int i = 0; i < 11; i++) {
            this.map.put(checkoutData.get(i), userData.get(i));
        }
    }

    public int getQuantityOfProducts(){
        int numberOfItems = 0;
        for (LineItem lineItem: lineItemList) {
            numberOfItems+= lineItem.getQuantity();
        }
        return numberOfItems;
    }

    public int getProductQuantity(int id) {
        for (LineItem lineItem: lineItemList) {
            if (lineItem.getProduct().getId() == id) {
                return lineItem.getQuantity();
            }
        }
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public List<LineItem> getProductList() {
        return lineItemList;
    }

    public Map<String, String> getOrderMap() {
        return map;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, ",
                this.id);

    }
}
