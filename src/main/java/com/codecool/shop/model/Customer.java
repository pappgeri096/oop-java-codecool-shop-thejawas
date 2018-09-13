package com.codecool.shop.model;

import java.util.*;

public class Customer extends BaseModel {

    List<String> customerInputData;

    public Customer(List<String> customerInputData) {
        this.customerInputData = customerInputData;
    }

    public List<String> getCustomerInputData() {
        return customerInputData;
    }
}
