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
public class ProductDaoSql extends BaseDaoSql implements ProductDao {

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
        return constructProductFromSqlData(query);
    }


    @Override
    public void remove(int id) {
        String query = "DELETE FROM product WHERE id ='" + id + "';";

        deleteRecordFromDatabase(query);
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM product;";

        ProductCategoryDao productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDaoMem = SupplierDaoMem.getInstance();

        List<Product> resultList = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Product product = constructProductFromSqlData(productCategoryDaoMem, supplierDaoMem, resultSet);
                resultList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }
    /**
     * Use this overloaded method to create only ONE product object from SQL records
     * */
    private Product constructProductFromSqlData(String query) {
        Product product = null;
        ProductCategoryDao productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDaoMem = SupplierDaoMem.getInstance();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDaoMem.find(resultSet.getInt("product_category_id")),
                        supplierDaoMem.find(resultSet.getInt("supplier_id"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Use this overloaded method to create several product objects in a while loop object from SQL records
     * */
    private Product constructProductFromSqlData(ProductCategoryDao productCategoryDao, SupplierDao supplierDao, ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("default_price"),
                resultSet.getString("default_currency"),
                resultSet.getString("description"),
                productCategoryDao.find(resultSet.getInt("product_category_id")),
                supplierDao.find(resultSet.getInt("supplier_id"))
        );
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

    private void insertProductWithValidation(String prePreparedQuery, String name, String description, BigDecimal defaultPrice,
            String defaultCurrency, int productCategoryId, int supplierId) {

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery);
        ) {
            Class.forName("org.postgresql.Driver");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setBigDecimal(3, defaultPrice);
            pstmt.setString(4, defaultCurrency);
            pstmt.setInt(5, productCategoryId);
            pstmt.setInt(6, supplierId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
