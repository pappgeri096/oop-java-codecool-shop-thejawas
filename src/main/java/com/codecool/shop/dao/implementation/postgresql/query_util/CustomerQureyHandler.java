package com.codecool.shop.dao.implementation.postgresql.query_util;

import com.codecool.shop.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerQureyHandler extends QueryHandler {

    protected int getLargestCustomerId() {
        String tableName = "public.\"user\"";
        String aggregateFunctionWithColumnName = "MAX(id)";
        String columnAlias = "largest_id";

        return getAggregateValueAsInt(aggregateFunctionWithColumnName, columnAlias, tableName);
    }

    protected void saveCustomerObject(Customer customer) {
        String prePreparedQuery = "INSERT INTO public.\"user\" (\n" +
                "  id,\n" +
                "  name,\n" +
                "  password_hash,\n" +
                "  email,\n" +
                "  phone_number,\n" +
                "  billing_country,\n" +
                "  billing_city,\n" +
                "  billing_zipcode,\n" +
                "  billing_address,\n" +
                "  shipping_country,\n" +
                "  shipping_city,\n" +
                "  shipping_zipcode,\n" +
                "  shipping_address\n" +
                ") VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String name = customer.getName();
        String email = customer.getEmail();
        String passwordHah = "5d9c68c6c50ed3d02a2fcf54f63993b6"; // TODO: CHANGE IT WHEN IMPLEMENTING USER REGISTRATION AND LOGIN
        int phoneNumber = customer.getPhoneNumber();
        String billingCountry = customer.getBillingCountry();
        String billingCity = customer.getBillingCity();
        String billingZipCode = customer.getBillingZipCode();
        String billingAddress = customer.getBillingAddress();
        String shippingCountry = customer.getShippingCountry();
        String shippingCity = customer.getShippingCity();
        String shippingZipCode = customer.getShippingZipCode();
        String shippingAddress = customer.getShippingAddress();


        DMLPreparedQueryForInsertingNewCustomer(
                prePreparedQuery, name, passwordHah, email, phoneNumber, billingCountry, billingCity, billingZipCode, billingAddress,
                shippingCountry, shippingCity, shippingZipCode, shippingAddress
        );
    }

    private void DMLPreparedQueryForInsertingNewCustomer(
            String prePreparedQuery,
            String name,
            String passwordHash,
            String email,
            int phoneNumber,
            String billingCountry,
            String billingCity,
            String billingZipCode,
            String billingAddress,
            String shippingCountry,
            String shippingCity,
            String shippingZipCode,
            String shippingAddress) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, email);
            pstmt.setInt(4, phoneNumber);
            pstmt.setString(5, billingCountry);
            pstmt.setString(6, billingCity);
            pstmt.setString(7, billingZipCode);
            pstmt.setString(8, billingAddress);
            pstmt.setString(9, shippingCountry);
            pstmt.setString(10, shippingCity);
            pstmt.setString(11, shippingZipCode);
            pstmt.setString(12, shippingAddress);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Customer createCustomerObjectBy(int customerId) throws IndexOutOfBoundsException {

        String query = "SELECT * FROM \"user\"\n" +
                        " WHERE id = " + customerId + ";";

        return getCustomers(query).get(0);
    }

    protected void updateCustomerInformation(
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
            String shippingAddress) {

        String preparedQuery = "UPDATE Customers\n" +
                "  name = ?,\n" +
                "  email = ?,\n" +
                "  phone_number = ?,\n" +
                "  billing_country = ?,\n" +
                "  billing_city = ?,\n" +
                "  billing_zipcode = ?,\n" +
                "  billing_address = ?,\n" +
                "  shipping_country = ?,\n" +
                "  shipping_city = ?,\n" +
                "  shipping_zipcode = ?,\n" +
                "  shipping_address = ?\n" +
                ") WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(preparedQuery)
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, phoneNumber);
            pstmt.setString(4, billingCountry);
            pstmt.setString(5, billingCity);
            pstmt.setString(6, billingZipCode);
            pstmt.setString(7, billingAddress);
            pstmt.setString(8, shippingCountry);
            pstmt.setString(9, shippingCity);
            pstmt.setString(10, shippingZipCode);
            pstmt.setString(11, shippingAddress);
            pstmt.setInt(12, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private List<Customer> getCustomers(String query) {
        List<Customer> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("phone_number"),
                        resultSet.getString("billing_country"),
                        resultSet.getString("billing_city"),
                        resultSet.getString("billing_zipcode"),
                        resultSet.getString("billing_address"),
                        resultSet.getString("shipping_country"),
                        resultSet.getString("shipping_city"),
                        resultSet.getString("shipping_zipcode"),
                        resultSet.getString("shipping_address")
                        );
                resultList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


}












