package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
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
public class ProductDaoSql extends BaseDaoSql implements ProductDao {

    private static ProductDaoSql singletonInstance = null;

    private ProductDaoSql() {
    }

    public static ProductDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public void add(Product product) {
        String prePreparedQuery = "INSERT INTO product (id, name, description, default_price, default_currency, product_category_id, supplier_id) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

        insertProductWithValidation(
                prePreparedQuery,
                product.getName(),
                product.getDescription(),
                product.getDefaulPrice(),
                String.valueOf(product.getDefaultCurrency()),
                product.getProductCategory().getId(),
                product.getSupplier().getId()
        );
    }

    @Override
    public Product find(int id) {
        String query = "SELECT * FROM product WHERE id ='" + id + "';";
        return getProducts(query).get(0);
    }


    @Override
    public void remove(int id) {
        String query = "DELETE FROM product WHERE id ='" + id + "';";

        deleteRecordFromDatabase(query);
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM product;";
        return getProducts(query);

    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT * FROM product WHERE supplier_id ='" + supplier.getId() + "';";
        return getProducts(query);

    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT * FROM product WHERE product_category_id ='" + productCategory.getId() + "';";
        return getProducts(query);
    }

    @Override
    public Product getBy(int id) {
        String query = "SELECT * FROM product WHERE id ='" + id + "';";
        return getProducts(query).get(0);
    }

    private List<Product> getProducts(String query) {
        List<Product> resultList = new ArrayList<>();

        ProductCategoryDao productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
        SupplierDao supplierDaoSql = SupplierDaoSql.getInstance();

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
                        productCategoryDaoSql.find(resultSet.getInt("product_category_id")),
                        supplierDaoSql.find(resultSet.getInt("supplier_id"))
                );
                resultList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    private int insertProductWithValidation(String prePreparedQuery, String name, String description, BigDecimal defaultPrice,
                                            String defaultCurrency, int productCategoryId, int supplierId) {
        int recordsAffected = 0;
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)) {
            Class.forName("org.postgresql.Driver");

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setBigDecimal(3, defaultPrice);
            pstmt.setString(4, defaultCurrency);
            pstmt.setInt(5, productCategoryId);
            pstmt.setInt(6, supplierId);

            recordsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Malformed SQL query. Query is rolled back");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recordsAffected;
    }

    public int getLastRecordsId(String query) {
        int lastItemsId = 0;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                lastItemsId = resultSet.getInt("max");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(lastItemsId);
        return lastItemsId;
    }
}
