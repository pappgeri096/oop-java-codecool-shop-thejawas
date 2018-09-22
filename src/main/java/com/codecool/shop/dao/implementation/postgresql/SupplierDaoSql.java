package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.postgresql.query_util.SupplierQueryHandler;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoSql extends SupplierQueryHandler implements SupplierDao {

    private static SupplierDaoSql instance = null;

    private SupplierDaoSql() {
    }

    public static SupplierDaoSql getInstance() {
        if (instance == null) {
            instance = new SupplierDaoSql();
        }
        return instance;
    }


    @Override
    public void add(Supplier objectType) {
        String prePreparedQuery = "INSERT INTO supplier (id, name, description) " +
                "VALUES (DEFAULT, ?, ?);";

        insertSupplierWithValidation(
                prePreparedQuery,
                objectType.getName(),
                objectType.getDescription()
        );
    }

    @Override
    public Supplier find(int id) {
        Supplier supplierObject = null;

        String query = "SELECT * FROM supplier WHERE id ='" + id + "';";


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            if (resultSet.next()) {
                int idFromDB = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                supplierObject = new Supplier(idFromDB, name, description);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplierObject;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM supplier WHERE id ='" + id + "';";

        executeInsertUpdateOrDelete(query);

    }

    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM supplier;";

        List<Supplier> resultList = new ArrayList<>();


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
                resultList.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }



}
