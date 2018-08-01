package com.codecool.shop.dao.implementation.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDaoSql {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/jawas_webshop";
    private static final String DB_USER = "jawas";
    private static final String DB_PASSWORD = "jawas";


    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    boolean deleteRecordFromDatabase(String query) {

        boolean success;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(query);
            Class.forName("org.postgresql.Driver");
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


}
