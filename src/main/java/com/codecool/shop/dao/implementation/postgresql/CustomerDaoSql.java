package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.util.*;

public class CustomerDaoSql implements CustomerDao {

    private static CustomerDaoSql instance = null;
    private List<Customer> data = new ArrayList<>();

    private List<String> checkoutData = Arrays.asList("name", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip"); // TODO: moves to customer
    private Map<String, String> customerDataMap = new HashMap<>();

    private CustomerDaoSql() {
    }

    public static CustomerDaoSql getInstance() {
        if (instance == null) {
            instance = new CustomerDaoSql();
        }
        return instance;
    }




    @Override
    public Customer getCurrent() {
        return null;
    }

    @Override
    public Map<String, String> getCustomerDataMap() {
        return null;
    }

    @Override
    public void createCustomerDataMap() {

    }

    @Override
    public void add(Customer objectType) {

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
