package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Product extends BaseModel {

    private BigDecimal defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;


    public Product(int id, String name, BigDecimal defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(id, name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public Product(String name, BigDecimal defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }


    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }

    private void setPrice(BigDecimal price, String currency) {
        this.defaultPrice = price.setScale(2, RoundingMode.HALF_UP);
        this.defaultCurrency = Currency.getInstance(currency);
    }

    public BigDecimal getDefaulPrice(){
        return defaultPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    private void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
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
