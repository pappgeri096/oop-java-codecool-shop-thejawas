package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LineItem {
    public int id;
    private Product product;
    private int quantity = 1;

    LineItem(int id, Product product){
        this.product = product;
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    void increaseQuantity(){
        quantity++;
    }

    public String getSubTotalPrice(){
        BigDecimal bigDecimalSubtotal = product.getDefaulPrice().multiply(new BigDecimal(quantity));
        BigDecimal subtotal = bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
        return subtotal.toString() + " " + product.getDefaultCurrency();
    }
}
