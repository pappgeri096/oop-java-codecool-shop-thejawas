package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.implementation.postgresql.query_util.CustomerQureyHandler;
import com.codecool.shop.model.Customer;
import com.codecool.shop.util.SessionUtil;

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
    public void updateCustomerInformation(int id, String name, String email, int phoneNumber, String billingCountry, String billingCity, String billingZipCode, String billingAddress, String shippingCountry, String shippingCity, String shippingZipCode, String shippingAddress) {
    }

    @Override
    public int getGuestId() {
        return getIdByName("Guest");
    }

    protected int getIdByName(String name) {

        String columnName = "id";
        String query = "SELECT " + columnName + " FROM \"user\"\n" +
                " WHERE name = '" + name + "';";
        return executeQueryWithColumnLabel_ReturnInt(query, columnName);
    }

    @Override
    public Customer find(int id) {
        return createCustomerObjectBy(id);
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

    @Override
    public Customer loginCustomer(String username, String password) {
        SessionUtil sessionUtil = SessionUtil.getInstance();
        Integer customerID = getCustomerID(username, password);

        if (customerID != null){
            sessionUtil.addAttributeToSession("customerId", customerID);
        }
        return null;
    }


}
