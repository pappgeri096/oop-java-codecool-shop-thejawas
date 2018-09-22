package com.codecool.shop.dao.implementation.postgresql.query_util;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.postgresql.ProductCategoryDaoSql;
import com.codecool.shop.dao.implementation.postgresql.SupplierDaoSql;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductQueryHandler extends QueryHandler {

    protected List<Product> getProducts(String query) {
        List<Product> resultList = new ArrayList<>();

        ProductCategoryDao productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
        SupplierDao supplierDaoSql = SupplierDaoSql.getInstance();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
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


    protected int insertProductWithValidation(String prePreparedQuery, String name, String description, BigDecimal defaultPrice,
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

    protected int getLastRecordsId(String query) {
        int lastItemsId = 0;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
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
