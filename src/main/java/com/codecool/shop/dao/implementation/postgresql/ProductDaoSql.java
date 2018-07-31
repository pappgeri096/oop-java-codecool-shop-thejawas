package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.math.BigDecimal;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ani on 2016.11.13..
 */
public class ProductDaoSql implements ProductDao {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/jawas_webshop";
    private static final String DB_USER = "jawas";
    private static final String DB_PASSWORD = "jawas";

    private static ProductDaoSql singletonInstance = null;

    private ProductDaoSql() {
    }

    public static ProductDaoSql getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public void add(Product product) {
        String prePreparedQuery = "INSERT INTO product (id, name, description, default_price, default_currency, product_category_id, supplier_id) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

        String productName = product.getName();
        String productDescription = product.getDescription();
        BigDecimal productDefaultPrice = product.getDefaulPrice();
        String productDefaultCurrency = String.valueOf(product.getDefaultCurrency());
        int productCategoryId = product.getProductCategory().getId(); // TODO: get id from database
        int supplierId = product.getSupplier().getId(); // TODO: get id from database

        insertWithValidation(
                prePreparedQuery, productName, productDescription, productDefaultPrice,
                productDefaultCurrency, productCategoryId, supplierId
        );

    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        ProductCategoryDao productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDaoMem = SupplierDaoMem.getInstance();

        String query = "SELECT * FROM product;";

        List<Product> resultList = new ArrayList<>();


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDaoMem.find(resultSet.getInt("product_category_id")),
                        supplierDaoMem.find(resultSet.getInt("supplier_id"))
                );
                resultList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }


    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }

    @Override
    public Product getBy(int id) {
        return null;
    }

    private void insertWithValidation(
            String prePreparedQuery, String name, String description, BigDecimal defaultPrice,
            String defaultCurrency, int productCategoryId, int supplierId) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery);
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setBigDecimal(3, defaultPrice);
            pstmt.setString(4, defaultCurrency);
            pstmt.setInt(5, productCategoryId);
            pstmt.setInt(6, supplierId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }
}
