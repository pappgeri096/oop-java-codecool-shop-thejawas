package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class CustomerDaoSql extends DaoSqlConnectionDML implements CustomerDao {
    private static CustomerDaoSql singletonInstance = null;

    private CustomerDaoSql() {
    }

    public static CustomerDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CustomerDaoSql();
        }
        return singletonInstance;
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
    public void createUserDataMap() {

    }

    @Override
    public void clearCustomerDataMap() {

    }

    @Override
    public void add(Customer objectType) {

    }

    @Override
    public Customer find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Customer> getAll() {
        return null;
    }
}
