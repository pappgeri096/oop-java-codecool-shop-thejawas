package com.codecool.shop.model;

import java.util.*;

public class Customer extends BaseModel {

    List<String> customerInputData;
//    private List<String> checkoutData = Arrays.asList("fullName", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip");

    public Customer(List<String> customerInputData) {
        this.customerInputData = customerInputData;
    }

    public List<String> getCustomerInputData() {
        return customerInputData;
    }
}
