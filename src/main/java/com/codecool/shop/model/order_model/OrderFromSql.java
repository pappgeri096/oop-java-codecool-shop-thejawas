package com.codecool.shop.model.order_model;

import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderFromSql extends BaseOrder {

    private String productName;
    private BigDecimal defaultPrice;
    private BigDecimal productQuantity;


    public OrderFromSql(int id, String productName, BigDecimal defaultPrice, BigDecimal productQuantity) {
        super(id);
        this.productName = productName;
        this.defaultPrice = defaultPrice;
        this.productQuantity = productQuantity;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void createUserDataMap(List<String> userData) {

    }

    @Override
    public Map<String, String> getUserDataMap() {
        return null;
    }

    @Override
    public List<LineItem> getLineItemList() {
        return null;
    }

    @Override
    public void createProductNameAndQuantityMaps() {

    }

    @Override
    public Map<String, Integer> getProductNameAndQuantityMap() {
        return null;
    }

    @Override
    public int getQuantityOfProducts() {
        return 0;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return null;
    }
}
