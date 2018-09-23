package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItem {
    public int id;
    private Product product;
    private int quantity = 1;

    public CartItem(int id, Product product){
        this.id = id;
        this.product = product;
    }

    public CartItem(int id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
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

    public void incrementQuantity(){
        quantity++;
    }

    public String getSubTotalPrice(){
        BigDecimal bigDecimalSubtotal = product.getDefaulPrice().multiply(new BigDecimal(quantity));
        BigDecimal subtotal = bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
        return subtotal.toString() + " " + product.getDefaultCurrency();
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
