package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;

import java.util.Map;

public interface CustomerDao extends BaseDAO<Customer> {

    Customer getCurrent();
    Map<String, String> getUserDataMap();
    void createUserDataMap();

//    void createProductNameAndQuantityMaps();
//    Map<String, Integer> getProductNameAndQuantityMap();


}
