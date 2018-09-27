package com.codecool.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Product extends BaseModel {

    private BigDecimal defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;

    public Product() {
    }

    public Product(int id, String name, BigDecimal defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(id, name, description);
        setDefaultPrice(defaultPrice);
        setDefaultCurrency(Currency.getInstance(currencyString));
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public Product(String name, BigDecimal defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        setDefaultPrice(defaultPrice);
        setDefaultCurrency(Currency.getInstance(currencyString));
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    @JsonIgnore
    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    private void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "defaultPrice: %3$f, " +
                        "defaultCurrency: %4$s, " +
                        "productCategory: %5$s, " +
                        "supplier: %6$s",
                this.id,
                this.name,
                this.defaultPrice,
                this.defaultCurrency.toString(),
                this.productCategory.getName(),
                this.supplier.getName());
    }
}
