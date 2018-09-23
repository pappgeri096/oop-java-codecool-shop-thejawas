package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.postgresql.query_util.ProductCategoryQueryHandler;
import com.codecool.shop.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoSql extends ProductCategoryQueryHandler implements ProductCategoryDao {

    private static ProductCategoryDaoSql instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoSql() {
    }

    public static ProductCategoryDaoSql getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoSql();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory objectType) {
        String prePreparedQuery = "INSERT INTO product_category (id, name, description, department) " +
                "VALUES (DEFAULT, ?, ?, ?);";

        insertProductCategoryWithValidation(
                prePreparedQuery,
                objectType.getName(),
                objectType.getDescription(),
                objectType.getDepartment()
        );

    }

    @Override
    public ProductCategory find(int id) {
        ProductCategory categoryObject = null;

        String query = "SELECT * FROM product_category WHERE id = '" + id + "';";


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            if (resultSet.next()) {
                int idFromDB = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String department = resultSet.getString("department");
                categoryObject = new ProductCategory(idFromDB, name, description, department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryObject;

    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_category WHERE id ='" + id + "';";

        DMLQuery(query);

    }

    @Override
    public List<ProductCategory> getAll() {

        String query = "SELECT * FROM product_category;";

        List<ProductCategory> resultList = new ArrayList<>();


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                ProductCategory category = new ProductCategory(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("department")
                );
                resultList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }

}
