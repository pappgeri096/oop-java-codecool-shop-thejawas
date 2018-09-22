package com.codecool.shop.dao.implementation.postgresql.query_util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductCategoryQueryHandler extends QueryHandler {

    protected void insertProductCategoryWithValidation(String prePreparedQuery, String name, String description, String department) {

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            Class.forName("org.postgresql.Driver");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, department);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



}
