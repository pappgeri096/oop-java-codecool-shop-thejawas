package com.codecool.shop.model.order_model;

import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class OrderFromMemory extends BaseOrder {

    Map<String, String> userDataMap = new HashMap<>();
    Map<String, Integer> productNameAndQuantityMap = new HashMap<>();
    private List<String> checkoutData = Arrays.asList("fullName", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip");

    private List<LineItem> lineItemList = new ArrayList<>();

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
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

    @Override
    public void createUserDataMap(List<String> userData) {
        for (int i = 0; i < 11; i++) {
            this.userDataMap.put(checkoutData.get(i), userData.get(i));
        }
    }

    @Override
    public Map<String, String> getUserDataMap() {
        return userDataMap;
    }

    @Override
    public int getQuantityOfProducts() {
        int numberOfItems = 0;
        for (LineItem lineItem : lineItemList) {
            numberOfItems += lineItem.getQuantity();
        }
        return numberOfItems;
    }

    @Override
    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    @Override
    public void createProductNameAndQuantityMaps() {
        for (LineItem lineItem:lineItemList) {
            productNameAndQuantityMap.put(lineItem.getProduct().getName(), lineItem.getQuantity());
        }
    }

    @Override
    public BigDecimal getTotalPrice() {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (LineItem lineItem : lineItemList) {
            sumPrice = lineItem.getProduct().getDefaultPrice().multiply(new BigDecimal(lineItem.getQuantity())).add(sumPrice);
        }
        BigDecimal totalPriceRounded = sumPrice.setScale(2, RoundingMode.HALF_UP);

        return totalPriceRounded;
    }

    @Override
    public Map<String, Integer> getProductNameAndQuantityMap() {
        return productNameAndQuantityMap;
    }

    @Override
    public String toString() {
        return String.format("OrderFromMemory ID: %1$d, ",
                this.id);

    }
}
