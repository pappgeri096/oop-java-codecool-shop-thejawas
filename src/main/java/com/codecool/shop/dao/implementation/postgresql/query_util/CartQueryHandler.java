package com.codecool.shop.dao.implementation.postgresql.query_util;

import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartQueryHandler extends QueryHandler {


    // TODO: SELECT QUERIES

    protected int getCurrentCartId(){
        String columnLabel = "largest_id";
        String query = "SELECT MAX(id) AS " + columnLabel + " FROM public.order;";
        return executeQuery_ReturnInt(query, columnLabel);
    }

    private int executeQuery_ReturnInt(String query, String columnLabel) {
        int cartId = 0;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if(resultSet.next()) {
                cartId = resultSet.getInt(columnLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartId;
    }

//    protected List<Integer> executeQuery_ReturnEachItemPriceBy(int cartId, String columnLabel) {
//
//        List<Integer> eachItemsPriceList = new ArrayList<>();
//
//        String query = "SELECT " + columnLabel + " FROM product \n" +
//                "FULL OUTER JOIN order_product op on product.id = op.product_id\n" +
//                "WHERE op.order_id = " + cartId + ";";
//
//        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            while (resultSet.next()) {
//                Integer defaultPrice = resultSet.getInt(columnLabel);
//                eachItemsPriceList.add(defaultPrice);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        return eachItemsPriceList;
//    }

    protected List<Integer> executeQueryWithColumnLabel(String query, String columnLabel) {

        List<Integer> eachItemsPriceList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Integer defaultPrice = resultSet.getInt(columnLabel);
                eachItemsPriceList.add(defaultPrice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return eachItemsPriceList;
    }

    protected void oldAddInsert(Cart cart) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = 1;
        String status = "unshipped";
        BigDecimal totalPrice = CartDaoMem.getInstance().getTotalPriceOfCurrentCart(); // TODO: USES MEMORY: REWRITE

        insertIntoTable_order(prePreparedQuery, userId, status, totalPrice);

        int orderId = getCurrentCartId();
        for (CartItem cartItem : cart.getCartItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = cartItem.getProduct().getId();
            int product_quantity = cartItem.getQuantity();
            insertIntoTable_order_product(prePreparedQuery, orderId, product_id, product_quantity);
        }
    }

    // TODO: DATA MANIPULATION - INSERT, UPDATE, DELETE

    protected void InsertCartWithProducts(Cart cart) { // TODO: REWRITE METHOD CALLS OF add() to
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = 1;
        String status = "unshipped";
        BigDecimal totalPrice = CartDaoMem.getInstance().getTotalPriceOfCurrentCart(); // TODO: USES MEMORY: REWRITE

        insertIntoTable_order(prePreparedQuery, userId, status, totalPrice);

        int orderId = getCurrentCartId();
        for (CartItem cartItem : cart.getCartItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = cartItem.getProduct().getId();
            int product_quantity = cartItem.getQuantity();
            insertIntoTable_order_product(prePreparedQuery, orderId, product_id, product_quantity);
        }
    }

    protected void insertIntoTable_order(String prePreparedQuery, int userId, String status, BigDecimal totalprice) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            pstmt.setBigDecimal(3, totalprice);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertIntoTable_order_product(String prePreparedQuery, int cartId, int productId, int quantity) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setInt(1, cartId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
