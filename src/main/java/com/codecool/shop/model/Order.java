package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        if (!wasProductFound){
            lineItemList.add(new LineItem(product));
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void createUserDataMap(List<String> userData) {
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

    public int getId() {
        return this.id;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }


    public void createProductsMaps(){
        for (LineItem lineItem:lineItemList) {
            productNameAndQuantityMap.put(lineItem.getProduct().name,lineItem.getQuantity());
        }
    }
    public BigDecimal getTotalPrice(){
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (LineItem lineItem : lineItemList) {
            sumPrice = lineItem.getProduct().getDefaultPrice().multiply(new BigDecimal(lineItem.getQuantity())).add(sumPrice);
        }
        BigDecimal totalPriceRounded = sumPrice.setScale(2, RoundingMode.HALF_UP);

        return totalPriceRounded;
    }

    public Map<String, String> getUserDataMap() {
        return userDataMap;
    }

    public Map<String, Integer> getProductNameAndQuantityMap() {
        return productNameAndQuantityMap;
    }

    @Override
    public String toString() {
        return String.format("Order ID: %1$d, ",
                this.id);

    }
}
