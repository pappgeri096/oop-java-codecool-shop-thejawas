package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.util.*;

public class CustomerDaoMem implements CustomerDao {

    private static CustomerDaoMem instance = null;
    private List<Customer> data = new ArrayList<>();

    private CustomerDaoMem() {
    }

    public static CustomerDaoMem getInstance() {
        if (instance == null) {
            instance = new CustomerDaoMem();
        }
        return instance;
    }


    @Override
    public int generateIdForNewCustomer() {
        return getData().size() + 1;
    }

    public List<Customer> getData() {
        return data;
    }

    @Override
    public void add(Customer objectType) {
        data.add(objectType);
    }

    @Override
    public Customer getCurrent() {
        return data.get(data.size() - 1);
    }

    @Override
    public boolean checkIfAnyCustomerDataMissing() {
        Customer customer = getCurrent();
        int id = customer.getId();
        String name = customer.getName();
        String email = customer.getEmail();
        int phoneNumber = customer.getPhoneNumber();
        String billingCountry = customer.getBillingCountry();
        String billingCity = customer.getBillingCity();
        String billingZipCode = customer.getBillingZipCode();
        String billingAddress = customer.getBillingAddress();
        String shippingCountry = customer.getShippingCountry();
        String shippingCity = customer.getShippingCity();
        String shippingZipCode = customer.getShippingZipCode();
        String shippingAddress = customer.getShippingAddress();

        boolean allDataIsProvided = id != 0 && name != null && email != null && phoneNumber != 0 &&
                billingCountry != null && billingCity != null && billingZipCode != null && billingAddress != null &&
                shippingCountry != null && shippingCity != null && shippingZipCode != null && shippingAddress != null;

        System.out.println(customer);
        System.out.println(allDataIsProvided);
        return !allDataIsProvided;
    }


    @Override
    public Customer find(int id) {
        // DOES NOTHING
        return null;
    }

    @Override
    public void remove(int id) {
        // DOES NOTHING
    }

    @Override
    public List<Customer> getAll() {
        // DOES NOTHING
        return null;
    }
}
