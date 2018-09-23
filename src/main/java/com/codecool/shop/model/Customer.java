package com.codecool.shop.model;

public class Customer extends BaseModel {

    private String passwordHash;
    private String email;
    private int phoneNumber;

    private String billingCountry;
    private String billingCity;
    private String billingZipCode;
    private String billingAddress;

    private String shippingCountry;
    private String shippingCity;
    private String shippingZipCode;
    private String shippingAddress;

    public Customer(int id) {
        super(id);
    }

    public Customer(
            int id, String name, String email, int phoneNumber,
            String billingCountry, String billingCity, String billingZipCode, String billingAddress,
            String shippingCountry, String shippingCity, String shippingZipCode, String shippingAddress
    ) {
        super(id, name);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingCountry = billingCountry;
        this.billingCity = billingCity;
        this.billingZipCode = billingZipCode;
        this.billingAddress = billingAddress;
        this.shippingCountry = shippingCountry;
        this.shippingCity = shippingCity;
        this.shippingZipCode = shippingZipCode;
        this.shippingAddress = shippingAddress;
    }

    public Customer(
            int id, String name, String passwordHash, String email, int phoneNumber,
            String billingCountry, String billingCity, String billingZipCode, String billingAddress,
            String shippingCountry, String shippingCity, String shippingZipCode, String shippingAddress
    ) {
        super(id, name);
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingCountry = billingCountry;
        this.billingCity = billingCity;
        this.billingZipCode = billingZipCode;
        this.billingAddress = billingAddress;
        this.shippingCountry = shippingCountry;
        this.shippingCity = shippingCity;
        this.shippingZipCode = shippingZipCode;
        this.shippingAddress = shippingAddress;
    }


    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public String getBillingZipCode() {
        return billingZipCode;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer ID=" + id +
                ", name=" + name +
                "email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", billingCountry='" + billingCountry + '\'' +
                ", billingCity='" + billingCity + '\'' +
                ", billingZipCode='" + billingZipCode + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", shippingCountry='" + shippingCountry + '\'' +
                ", shippingCity='" + shippingCity + '\'' +
                ", shippingZipCode='" + shippingZipCode + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                "} ";
    }
}
