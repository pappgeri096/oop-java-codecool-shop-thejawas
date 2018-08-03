package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LineItem {
    private static int idCounter=1;
    public int id;
    private Product product;
    private int quantity = 1;

    public LineItem(Product product){
        this.product = product;
        id = idCounter;
        idCounter++;

    }

    public void setId(int id) {
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

    public void increaseQuantity(){
        quantity++;
    }

    public String getSubTotalPrice(){
        BigDecimal bigDecimalSubtotal = product.getDefaulPrice().multiply(new BigDecimal(quantity));
        BigDecimal subtotal = bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
        return subtotal.toString() + " " + product.getDefaultCurrency();
    }
}
