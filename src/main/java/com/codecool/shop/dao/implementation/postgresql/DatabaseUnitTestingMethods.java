package com.codecool.shop.dao.implementation.postgresql;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;



public class DatabaseUnitTestingMethods {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/jawas_webshop";
    private static final String DB_USER = "jawas";
    private static final String DB_PASSWORD = "jawas";
    private static final String PRODUCT_CATEGORY_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_product_category_db_dump.sql";
    private static final String SUPPLIER_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_supplier_db_dump.sql";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_procuct_db_dump.sql";
    private static final String USER_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_user_dump.sql";
    private static final String ORDER_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_order.sql";
    private static final String ORDER_PRODUCT_FILE_PATH = "src/main/resources/sql/db_dumps/jawas_webshop_public_order_product.sql";
    private static final String CREATE_DB_SCHEMA = "src/main/resources/sql/db_dumps/jawas_webshop_create_db_schema.sql";

    public DatabaseUnitTestingMethods() {
        executeUpdateFromFile(CREATE_DB_SCHEMA);
        executeUpdateFromFile(PRODUCT_CATEGORY_FILE_PATH);
        executeUpdateFromFile(SUPPLIER_FILE_PATH);
        executeUpdateFromFile(PRODUCT_FILE_PATH);
        executeUpdateFromFile(USER_FILE_PATH);
        executeUpdateFromFile(ORDER_FILE_PATH);
        executeUpdateFromFile(ORDER_PRODUCT_FILE_PATH);
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    DATABASE,
                    DB_USER,
                    DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR: Connection error.");
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String query) throws SQLException {
        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLTimeoutException e) {
            System.err.println("ERROR: SQL Timeout");
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();

        } catch (SQLTimeoutException e) {
            System.err.println("ERROR: SQL Timeout");
        }
        return null;
    }

    public void copyDataFromFile(String tableName, String filePath) throws SQLException {
        FileReader fileReader = null;

        try (Connection connection = getConnection()) {

            try {
                fileReader = new FileReader(filePath);
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: File not found!");
            }

            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            try {
                copyManager.copyIn("COPY " + tableName + " FROM STDIN", fileReader);
            } catch (IOException e) {
                System.err.println("ERROR: IO Error occured!");
                e.printStackTrace();
            }

        } catch (SQLTimeoutException e) {
            System.err.println("ERROR: SQL Timeout");
        }
    }

    public void executeUpdateFromFile(String filePath) {
        String query = "";
        try {
            query = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseUnitTestingMethods dbObject = new DatabaseUnitTestingMethods();
        dbObject.getConnection();
    }

}
