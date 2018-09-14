package com.codecool.shop.model;

import java.util.*;

public class Customer extends BaseModel {

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


    private List<String> customerInputData;

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

    public Customer(List<String> customerInputData) { // TODO: ASSIGN USER INPUT DATA TO FIELD VARIABLES
        this.customerInputData = customerInputData;
    }

    public List<String> getCustomerInputData() {
        return customerInputData;
    }
}
