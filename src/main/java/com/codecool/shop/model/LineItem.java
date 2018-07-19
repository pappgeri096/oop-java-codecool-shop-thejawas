package com.codecool.shop.model;

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

    public String getSumPrice(){
        return String.valueOf(product.getDefaulPrice()*quantity)+" "+ product.getDefaultCurrency();
    }
}
