package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ani on 2016.11.13..
 */
public class ProductDaoJdbc implements ProductDao {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/jawas_webshop";
    private static final String DB_USER = "akincsei";
    private static final String DB_PASSWORD = "assklyuelleis_6";

    private static ProductDaoJdbc singletonInstance = null;

    private ProductDaoJdbc() {
    }

    public static ProductDaoJdbc getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductDaoJdbc();
        }
        return singletonInstance;
    }

    @Override
    public void add(Product product) {
        String prePreparedQuery = "INSERT INTO product (id, name, description, default_price, default_currency, product_category_id, supplier_id) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

        String productName = product.getName();
        String productDescription = product.getDescription();
        float productDefaultPrice = product.getDefaulPrice();
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
        // TODO: SELECT ALL PRODUCT CATEGORY AND SUPPLIER DATA
        // todo: MAYBE IN A SEPARATE QUERY
        String query = "SELECT * FROM product;";

        List<Product> resultList = new ArrayList<>();


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
//            while (resultSet.next()) {
//                Product product = new Product(
//                        resultSet.getString("name"),
//                        resultSet.getFloat("default_price"),
//                        resultSet.getString("default_currency"),
//                        resultSet.getString("description"),
//                        resultSet.getInt("product_category_id"),  // TODO: get name from database by id
//                        resultSet.getInt("supplier_id")   // TODO: get name from database by id
//                );
//                resultList.add(product);
//            }
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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWithValidation(
            String prePreparedQuery, String name, String description, float defaultPrice,
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
            pstmt.setFloat(3, defaultPrice);
            pstmt.setString(4, defaultCurrency);
            pstmt.setInt(5, productCategoryId);
            pstmt.setInt(6, supplierId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


//    @Override
//    public void add(Todo todo) {
//        String prePreparedQuery = "INSERT INTO todos (title, id, status) " +
//                "VALUES (?, '" + todo.id + "', '" + todo.status + "');";
//
//        insertWithValidation(prePreparedQuery, todo.title);
//    }
//
//    @Override
//    public Todo find(String id) {
//
//        String query = "SELECT * FROM todos WHERE id ='" + id + "';";
//
//        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query);
//        ) {
//            if (resultSet.next()) {
//                Todo result = new Todo(resultSet.getString("title"),
//                        resultSet.getString("id"),
//                        Status.valueOf(resultSet.getString("status")));
//                return result;
//            } else {
//                return null;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    public void update(String id, String title) {
//        String prePreparedQuery = "UPDATE todos SET title = ? WHERE id = '" + id + "';";
//
//        insertWithValidation(prePreparedQuery, title);
//
//    }
//
//
//
//    @Override
//    public List<Todo> ofStatus(String statusString) {
//        return (statusString == null || statusString.isEmpty()) ?
//                all() : ofStatus(Status.valueOf(statusString.toUpperCase()));
//    }
//
//    @Override
//    public List<Todo> ofStatus(Status status) {
//        String query = "SELECT * FROM todos WHERE status ='" + status + "';";
//        ;
//
//        List<Todo> resultList = new ArrayList<>();
//
//        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query);
//        ) {
//            while (resultSet.next()) {
//                Todo actTodo = new Todo(resultSet.getString("title"),
//                        resultSet.getString("id"),
//                        Status.valueOf(resultSet.getString("status")));
//                resultList.add(actTodo);
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return resultList;
//    }
//
//    @Override
//    public void remove(String id) {
//        String query = "DELETE FROM todos WHERE id = '" + id + "';";
//        executeQuery(query);
//    }
//
//    @Override
//    public void removeCompleted() {
//        String query = "DELETE FROM todos WHERE status = '" + Status.COMPLETE + "';";
//        executeQuery(query);
//    }
//
//    @Override
//    public void toggleStatus(String id) {
//        Todo todo = find(id);
//
//        if (null == todo) {
//            return;
//        }
//
//        Status newStatus = (todo.status == Status.ACTIVE) ? Status.COMPLETE : Status.ACTIVE;
//        String query = "UPDATE todos SET status = '" + newStatus + "' WHERE id = '" + id + "';";
//        executeQuery(query);
//
//    }
//
//    @Override
//    public void toggleAll(boolean complete) {
//        Status newStatus = complete ? Status.COMPLETE : Status.ACTIVE;
//        String query = "UPDATE todos SET status = '" + newStatus + "';";
//        executeQuery(query);
//    }
//
//
//    // package private so test can see it, but TodoList not
//    void deleteAll() {
//        String query = "DELETE FROM todos;";
//        executeQuery(query);
//    }
//

    public static void test() {
        System.out.println("mukodj!");;
    }

    public static void main(String[] args) {

    }
}
