package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.implementation.postgresql.query_util.CustomerQureyHandler;
import com.codecool.shop.model.Customer;

import java.util.*;

public class CustomerDaoSql extends CustomerQureyHandler implements CustomerDao {

    private static CustomerDaoSql instance = null;

    private CustomerDaoSql() {
    }

    public static CustomerDaoSql getInstance() {
        if (instance == null) {
            instance = new CustomerDaoSql();
        }
        return instance;
    }

    @Override
    public void add(Customer objectType) {
        saveCustomerObject(objectType);
    }

    @Override
    public int generateIdForNewCustomer() {
        return getLargestCustomerId() + 1;
    }

    @Override
    public Customer getCurrent() throws IndexOutOfBoundsException, NullPointerException {
        return createCustomerObjectBy(getLargestCustomerId());
    }

    @Override
    public boolean checkIfAnyCustomerDataMissing() {
        int largestUserIdInDatabase = getLargestCustomerId();
        boolean anyDataMissing = true;
        try {
            Customer customer = createCustomerObjectBy(largestUserIdInDatabase);
            customer.getId();
            anyDataMissing = false;
        } catch (IndexOutOfBoundsException ioube) {
            System.out.println("No customer exist in database with an ID: " + largestUserIdInDatabase);
            System.out.println(ioube.getMessage());
            ioube.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("No customer exist in database with an ID: " + largestUserIdInDatabase);
            System.out.println(npe.getMessage());
            npe.printStackTrace();
        }
        return anyDataMissing;
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
