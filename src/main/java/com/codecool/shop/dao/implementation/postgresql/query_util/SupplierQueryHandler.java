package com.codecool.shop.dao.implementation.postgresql.query_util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupplierQueryHandler extends QueryHandler {

    protected void insertSupplierWithValidation(String prePreparedQuery, String name, String description) {

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            Class.forName("org.postgresql.Driver");
            pstmt.setString(1, name);
            pstmt.setString(2, description);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("SQL exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound exception");
            e.printStackTrace();
        }

    }


}
