package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.order_model.BaseOrder;
import com.codecool.shop.model.order_model.LineItem;
import com.codecool.shop.model.order_model.OrderFromMemory;
import com.codecool.shop.model.order_model.OrderFromSql;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoSql extends BaseDaoSql implements OrderDao {

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
    public OrderFromMemory getCurrent() {
        String query = "SELECT * FROM order WHERE id ='" + getCurrentOrderId() + "';";
        return null; // TODO----------------------------------
    }

    List<BaseOrder> getOrdersBy(int userId) {
        String query = "SELECT\n" +
                "  \"order\".id AS id_from_order,\n" +
                "  p.name,\n" +
                "  p.default_price,\n" +
                "  op.product_quantity\n" +
                "FROM \"order\"\n" +
                "  FULL OUTER JOIN order_product op on \"order\".id = op.order_id\n" +
                "  FULL OUTER JOIN product p on op.product_id = p.id\n" +
                "WHERE \"order\".user_id = 1\n" +
                "ORDER BY id_from_order;";
        return getOrders(query);
    }

    private List<BaseOrder> getOrders(String query) {
        List<BaseOrder> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                BaseOrder order = new OrderFromSql(
                        resultSet.getInt("id_from_order"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("default_price"),
                        resultSet.getBigDecimal("product_quantity")
                );
                resultList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public void add(BaseOrder orderMem) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = 1;
        String status = "unshipped";
        BigDecimal totalPrice = orderMem.getTotalPrice();

        addToOrderSql(prePreparedQuery, userId, status, totalPrice);

        int orderId = getCurrentOrderId();
        for (LineItem lineItem: ((OrderFromMemory) orderMem).getLineItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = lineItem.getProduct().getId();
            int product_quantity = lineItem.getQuantity();
            addToOrder_ProductSql(prePreparedQuery, orderId, product_id, product_quantity);
        }
    }

    @Override
    public OrderFromMemory find(int id) {
        return null; // TODO----------------------------------
    }

    @Override
    public void remove(int id) {
        // TODO----------------------------------
    }

    @Override
    public List<BaseOrder> getAll() {
        return null; // TODO----------------------------------
    }

    private void addToOrderSql(String prePreparedQuery, int userId, String status, BigDecimal totalprice) {
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
    private int getCurrentOrderId(){
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
}
