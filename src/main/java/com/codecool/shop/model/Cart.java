package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

// TODO: DOES NOT ID USE NAME AND DESCRIPTION FIELDS. calling getName() and getDescription() methods can cause problems
public class Cart extends BaseModel {

    private List<LineItem> lineItemList = new ArrayList<>();

    public void addProduct(Product product) {
        boolean wasProductFound = false;
        for (LineItem lineItem : lineItemList) {
            if (lineItem.getProduct().getId() == (product.getId())) {
                lineItem.increaseQuantity();
                wasProductFound = true;
            }
        }
        if (!wasProductFound){
            lineItemList.add(new LineItem(createIdForLineItem(), product));
        }

    }

    private int createIdForLineItem() {
        return lineItemList.size();
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public int getQuantityOfProducts() {
        int numberOfItems = 0;
        for (LineItem lineItem : lineItemList) {
            numberOfItems += lineItem.getQuantity();
        }
        return numberOfItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "lineItemList=" + lineItemList +
                "}";
    }
}
