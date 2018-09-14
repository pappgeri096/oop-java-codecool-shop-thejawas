package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.util.StatusType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

import com.codecool.shop.dao.implementation.postgresql.query_util.CartQueryHandler;

public class CartDaoSql extends CartQueryHandler implements CartDao {
    private static CartDaoSql singletonInstance = null;



    private CartDaoSql() {
    }

    public static CartDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CartDaoSql();
        }
        return singletonInstance;
    }

    /**
     * Initializes empty Cart with an ID.
     * */
    @Override
    public void add(Cart cart) { // TODO: REWRITE ACCORDING TO DOCUMENTATION. DO NOT FORGET ABOUT ITS METHOD CALLS
        oldAddInsert(cart);
    }


    public void insertEmptyCart(int userId, StatusType statusType, BigDecimal totalPrice) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        insertIntoTable_order(prePreparedQuery, userId, statusType.getStatusString(), totalPrice);

//        int orderId = getCurrentCartId();
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
        Cart currentCart;

        if (getCurrentCartId() == 0) {
            currentCart = new Cart();
        } else { // TODO NEXT: WRITE QUERY FOR CREATING A CART FROM DATABASE
            String query = "SELECT * FROM order WHERE id ='" + getCurrentCartId() + "';";
            currentCart = new Cart(); // temporary
        }

        return currentCart;
    }

    @Override
    public BigDecimal getTotalPriceOfCurrentCart() { // TODO: USES MEMORY: REWRITE
        int currentCartId = getCurrentCartId();

        String query = "SELECT default_price \n" +
                "FROM product \n" +
                "FULL OUTER JOIN order_product op on product.id = op.product_id \n" +
                "WHERE op.order_id = " + currentCartId + ";";

        String columnLabel = "default_price";

        List<Integer> eachItemsPriceList = executeQueryWithColumnLabel(query, columnLabel);
        Integer integerSum = eachItemsPriceList.stream().mapToInt(i -> i).sum();
        return BigDecimal.valueOf(integerSum).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void createProductNameAndQuantityMaps() {
        // TODO???????????
    }

    @Override
    public Map<String, Integer> getProductNameAndQuantityMap() {
        return null;
    }

    List<Cart> getCartBy(int userId) {
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
        return getCarts(query);
    }

    private List<Cart> getCarts(String query) {
        List<Cart> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Cart cart = new Cart();
                resultList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }





    @Override
    public void clearProductNameAndQuantityMap() {
        // TODO
    }
}
