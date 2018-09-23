package com.codecool.shop.dao.implementation.postgresql.query_util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryHandler extends DatabaseConnectionHandler {

    // METHODS EXECUTING DATA MANIPULATION LANGUAGE STATEMENTS: INSERT, UPDATE, DELETE

    protected boolean DMLQuery(String query) {

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


    // SELECT QUERIES


    /**
     * Example:
     * String tableName = "public.order";
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

    protected List<Integer> executeQueryWithColumnLabel_ReturnIntegerList(String query, String columnLabel) {

        List<Integer> eachItemsPriceList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Integer requiredField = resultSet.getInt(columnLabel);
                eachItemsPriceList.add(requiredField);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return eachItemsPriceList;
    }



}
