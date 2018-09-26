package com.codecool.shop.dao.implementation.postgresql.query_util;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryHandler extends DatabaseConnectionHandler {

    // METHODS EXECUTING DATA MANIPULATION LANGUAGE STATEMENTS: INSERT, UPDATE, DELETE

    protected boolean DMLexecute(String query) {

        int numberOfRowsAffected;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            numberOfRowsAffected = statement.executeUpdate(query);
            Class.forName("org.postgresql.Driver");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            numberOfRowsAffected = -1;
        }

        return numberOfRowsAffected == 1;
    }

    protected void DMLPreparedQuery3Parameters(String prePreparedQuery, int firstParameter, int secondParameter, int thirdParameter) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setInt(1, firstParameter);
            pstmt.setInt(2, secondParameter);
            pstmt.setInt(3, thirdParameter);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void DMLPreparedQuery4Parameters(String prePreparedQuery, int firstParameter, String secondParameter, int thirdParameter, int fourthParameter) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setInt(1, firstParameter);
            pstmt.setString(2, secondParameter);
            pstmt.setInt(3, thirdParameter);
            pstmt.setInt(4, fourthParameter);


            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // SELECT QUERIES


    /**
     * Example:
     * String tableName = "public.\"order\"";
     * String aggregateFunctionWithColumnName = "MAX(id)";
     * String columnAlias = "largest_id";
     *
     * getAggregateValueAsInt(aggregateFunctionWithColumnName, columnAlias, tableName);
     * */
    protected int getAggregateValueAsInt(String aggregateFunctionWithColumnName, String columnAlias, String tableName) {
        String query = "SELECT " + aggregateFunctionWithColumnName + " AS " + columnAlias + " FROM " + tableName + ";";
        int requiredInt = 0;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if(resultSet.next()) {
                requiredInt = resultSet.getInt(columnAlias);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requiredInt;
    }

    protected List<Double> executeQueryWithColumnLabel_ReturnIntegerList(String query, String columnLabel) {

        List<Double> eachItemsPriceList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Double requiredField = resultSet.getDouble(columnLabel);
                eachItemsPriceList.add(requiredField);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return eachItemsPriceList;
    }

    protected String executeQueryWithColumnLabelById_ReturnString(String query, String columnLabel) {

        String requiredField = null;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                requiredField = resultSet.getString(columnLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requiredField;
    }

    protected BigDecimal executeQueryWithColumnLabel_ReturnBigDecimal(String query, String columnLabel) {

        BigDecimal requiredField = null;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                requiredField = resultSet.getBigDecimal(columnLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requiredField;
    }

    protected int executeQueryWithColumnLabel_ReturnInt(String query, String columnLabel) {

        int requiredField = 0;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                requiredField = resultSet.getInt(columnLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requiredField;
    }


}
