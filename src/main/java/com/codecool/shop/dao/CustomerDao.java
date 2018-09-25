package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;

public interface CustomerDao extends BaseDAO<Customer> {

    int generateIdForNewCustomer();
    Customer getCurrent();
    boolean checkIfAnyCustomerDataMissing();

    int getGuestId();
}
