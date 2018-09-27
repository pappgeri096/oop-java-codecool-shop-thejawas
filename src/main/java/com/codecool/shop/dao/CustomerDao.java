package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;

public interface CustomerDao extends BaseDAO<Customer> {

    int generateIdForNewCustomer();
    Customer getCurrent();
    boolean checkIfAnyCustomerDataMissing();
    void updateCustomerInformation(
            int id,
            String name,
            String email,
            int phoneNumber,
            String billingCountry,
            String billingCity,
            String billingZipCode,
            String billingAddress,
            String shippingCountry,
            String shippingCity,
            String shippingZipCode,
            String shippingAddress);

    int getGuestId();
}
