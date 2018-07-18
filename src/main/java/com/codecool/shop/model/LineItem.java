package com.codecool.shop.model;

public class LineItem {
    public int id;
    private Product product;
    private int quantity = 1;

    public LineItem(Product product){
        this.product = product;
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
}
