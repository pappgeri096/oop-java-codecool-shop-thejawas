package com.codecool.shop.model;

import java.util.*;

public class Order {
    private int id;
    Map<String, String> userDataMap = new HashMap<>();
    Map<String, Integer> productNameAndQuantityMap = new HashMap<>();
    private List<String> checkoutData = Arrays.asList("fullName", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip");

    private List<LineItem> lineItemList;

    public Order() {
        lineItemList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        boolean wasProductFound = false;
        for (LineItem lineItem : lineItemList) {
            if (lineItem.getProduct().getId() == (product.getId())) {
                lineItem.increaseQuantity();
                wasProductFound = true;
            }
        }
        if (!wasProductFound) {
            lineItemList.add(new LineItem(product));
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserData(List<String> userData) {
        for (int i = 0; i < 11; i++) {
            this.userDataMap.put(checkoutData.get(i), userData.get(i));
        }
    }

    public int getQuantityOfProducts() {
        int numberOfItems = 0;
        for (LineItem lineItem : lineItemList) {
            numberOfItems += lineItem.getQuantity();
        }
        return numberOfItems;
    }

    public int getProductQuantity(int id) {
        for (LineItem lineItem : lineItemList) {
            if (lineItem.getProduct().getId() == id) {
                return lineItem.getQuantity();
            }
        }
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }


    public void makeProductsMaps() {
        for (LineItem lineItem : lineItemList) {
            productNameAndQuantityMap.put(lineItem.getProduct().name, lineItem.getQuantity());
        }
    }

    public float getPriceOfAllProducts() {
        float sumPrice = 0;
        Currency currency = null;
        for (LineItem lineItem : lineItemList) {
            sumPrice = sumPrice + lineItem.getProduct().getDefaultPrice() * lineItem.getQuantity();
            currency = lineItem.getProduct().getDefaultCurrency();
        }

        return sumPrice;
    }

    public Map<String, String> getUserDataMap() {
        return userDataMap;
    }

    public Map<String, Integer> getProductNameAndQuantityMap() {
        return productNameAndQuantityMap;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, ",
                this.id);

    }
}
