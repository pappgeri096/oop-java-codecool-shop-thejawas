package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class OrderDaoSql implements OrderDao {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/jawas_webshop";
    private static final String DB_USER = "jawas";
    private static final String DB_PASSWORD = "jawas";

    private static OrderDaoSql singletonInstance = null;

    private OrderDaoSql() {
    }

    public static OrderDaoSql getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new OrderDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public Order getCurrent() {
        return null;
    }

    @Override
    public void add(Order order) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = 1;
        String status = "unshipped";
        BigDecimal totalPrice = order.getTotalPrice();

        addToOrderSql(prePreparedQuery, userId, status, totalPrice);

        int orderId = getOrder_id();
        for (LineItem lineItem:order.getLineItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = lineItem.getProduct().getId();
            int product_quantity = lineItem.getQuantity();
            addToOrder_ProductSql(prePreparedQuery,orderId,product_id,product_quantity);
        }
    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    private void addToOrderSql(String prePreparedQuery, int userId, String status,BigDecimal totalprice) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery);
        ) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            pstmt.setBigDecimal(3, totalprice);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private int getOrder_id(){
        String query = "SELECT MAX(id) FROM public.order;";

        int orderId = 0;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if(resultSet.next()) {
                orderId = resultSet.getInt("max");
             }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }



    private void addToOrder_ProductSql(String prePreparedQuery, int orderId, int productId, int quantity) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery);
        ) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);

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
