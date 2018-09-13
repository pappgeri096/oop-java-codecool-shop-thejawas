package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

public class CartDaoSql extends DaoSqlConnectionDML implements CartDao {
    private static CartDaoSql singletonInstance = null;

    private CartDaoSql() {
    }

    public static CartDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CartDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public void add(Cart cart) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = 1;
        String status = "unshipped";
        BigDecimal totalPrice = CartDaoMem.getInstance().getTotalPrice(); // TODO: USES MEMORY: REWRITE

        addToOrderSql(prePreparedQuery, userId, status, totalPrice);

        int orderId = getCurrentOrderId();
        for (CartItem cartItem : cart.getCartItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = cartItem.getProduct().getId();
            int product_quantity = cartItem.getQuantity();
            addToOrder_ProductSql(prePreparedQuery, orderId, product_id, product_quantity);
        }
    }

    @Override
    public Cart find(int id) {
        return null; // TODO----------------------------------
    }

    @Override
    public void remove(int id) {
        // TODO----------------------------------
    }

    @Override
    public List<Cart> getAll() {
        return null; // TODO----------------------------------
    }

    @Override
    public Cart getCurrent() {
        String query = "SELECT * FROM order WHERE id ='" + getCurrentOrderId() + "';";
        return null; // TODO----------------------------------
    }

    @Override
    public BigDecimal getTotalPrice() { // TODO: USES MEMORA: REWRITE
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : getCurrent().getCartItemList()) {
            sumPrice = cartItem.getProduct().getDefaultPrice().multiply(new BigDecimal(cartItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void createProductNameAndQuantityMaps() {

    }

    @Override
    public Map<String, Integer> getProductNameAndQuantityMap() {
        return null;
    }

    List<Cart> getOrdersBy(int userId) {
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

    private List<Cart> getOrders(String query) {
        List<Cart> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Cart order = new Cart();
                resultList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    private void addToOrderSql(String prePreparedQuery, int userId, String status, BigDecimal totalprice) {
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
             PreparedStatement pstmt = connection.prepareStatement(prePreparedQuery)
        ) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clearProductNameAndQuantityMap() {

    }
}
