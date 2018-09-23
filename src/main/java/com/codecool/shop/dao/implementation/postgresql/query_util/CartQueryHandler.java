package com.codecool.shop.dao.implementation.postgresql.query_util;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.util.CartStatusType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartQueryHandler extends QueryHandler {


    // TODO: SELECT QUERIES

    protected int getLastCartId(){
        String tableName = "public.order";
        String aggregateFunctionWithColumnName = "MAX(id)";
        String columnAlias = "largest_id";

        return getAggregateValueAsInt(aggregateFunctionWithColumnName, columnAlias, tableName);
    }

    public BigDecimal getTotalPriceOfLastCart() {

        String columnLabel = "default_price";

        String query = "SELECT" + columnLabel + " FROM product \n" +
                "FULL OUTER JOIN order_product op on product.id = op.product_id \n" +
                "WHERE op.order_id = " + getLastCartId() + ";";

        List<Integer> eachItemsPriceList = executeQueryWithColumnLabel_ReturnIntegerList(query, columnLabel);

        Integer integerSum = eachItemsPriceList
                .stream()
                .mapToInt(i -> i)
                .sum();

        return BigDecimal.valueOf(integerSum).setScale(2, RoundingMode.HALF_UP);
    }

    protected Cart getCartByOrder(int id) {
        String query = "SELECT\n" +
                "  \"order\".id AS id_from_order,\n" +
                "  \"order\".user_id AS id_from_user,\n" +
                "  \"order\".status,\n" +
                "  p.id AS id_from_product,\n" +
                "  p.name,\n" +
                "  p.default_price,\n" +
                "  op.product_quantity\n" +
                "FROM \"order\"\n" +
                "  FULL OUTER JOIN order_product op on \"order\".id = op.order_id\n" +
                "  FULL OUTER JOIN product p on op.product_id = p.id\n" +
                "WHERE \"order\".id = '" + id + "'\n" +
                "ORDER BY id_from_order;";
        return getCarts(query).get(0);
    }

    protected List<Cart> getCartsByUser(int id) {
        String query = "SELECT\n" +
                "  \"order\".id AS id_from_order,\n" +
                "  \"order\".user_id AS id_from_user,\n" +
                "  \"order\".status,\n" +
                "  p.id AS id_from_product,\n" +
                "  p.name,\n" +
                "  p.default_price,\n" +
                "  op.product_quantity\n" +
                "FROM \"order\"\n" +
                "  FULL OUTER JOIN order_product op on \"order\".id = op.order_id\n" +
                "  FULL OUTER JOIN product p on op.product_id = p.id\n" +
                "WHERE \"order\".user_id = '" + id + "'\n" +
                "ORDER BY id_from_order;";
        return getCarts(query);
    }

    protected List<Cart> getAllCarts() {
        String query = "SELECT\n" +
                "  \"order\".id AS id_from_order,\n" +
                "  \"order\".user_id AS id_from_user,\n" +
                "  \"order\".status,\n" +
                "  p.id AS id_from_product,\n" +
                "  p.name,\n" +
                "  p.default_price,\n" +
                "  op.product_quantity\n" +
                "FROM \"order\"\n" +
                "  FULL OUTER JOIN order_product op on \"order\".id = op.order_id\n" +
                "  FULL OUTER JOIN product p on op.product_id = p.id\n" +
                "ORDER BY id_from_order;";
        return getCarts(query);
    }


    private List<Cart> getCarts(String query) {
        List<Cart> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {

            ProductDao productDataManager = Initializer.getImplementationFactory().getProductDataManagerInstance();

            Cart cart;
            int cartItemCounter = 1;
            int cartId;
            int previousCartId = -1;
            while (resultSet.next()) {
                cartId = resultSet.getInt("id_from_order");

                if (previousCartId != cartId) {
                    cartItemCounter = 1;
                    cart = new Cart(
                            resultSet.getInt("id_from_order"),
                            resultSet.getInt("id_from_user"),
                            CartStatusType.valueOf(resultSet.getString("status"))
                    );

                    CartItem cartItem = new CartItem(
                            cartItemCounter,
                            productDataManager.find(resultSet.getInt("id_from_product")),
                            resultSet.getInt("product_quantity")
                    );

                    cart.addToCartItemList(cartItem);

                    resultList.add(cart);

                } else {
                    CartItem cartItem = new CartItem(
                            cartItemCounter++,
                            productDataManager.find(resultSet.getInt("id_from_product")),
                            resultSet.getInt("product_quantity")
                    );

                    resultList.get(resultList.size() - 1).addToCartItemList(cartItem);

                }

                previousCartId = cartId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }


    // TODO: DATA MANIPULATION - INSERT, UPDATE, DELETE

    protected void insertInto_order_And_order_product(Cart cart) { // TODO: REWRITE METHOD CALLS OF add() to
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        int userId = cart.getUserId();
        String status = cart.getCartStatusType().getStatusString();
        BigDecimal totalPrice = getTotalPriceOfLastCart();

        insertIntoTable_order(prePreparedQuery, userId, status, totalPrice);

        int orderId = getLastCartId();
        for (CartItem cartItem : cart.getCartItemList()) {

            prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                    "VALUES (DEFAULT, ?, ?, ?);";
            int product_id = cartItem.getProduct().getId();
            int product_quantity = cartItem.getQuantity();
            DMLPreparedQuery3Parameters(prePreparedQuery, orderId, product_id, product_quantity);
        }
    }

    public void insertEmptyCart(int userId, CartStatusType cartStatusType, BigDecimal totalPrice) {
        String prePreparedQuery = "INSERT INTO public.order (id, user_id, status, total_price)" +
                "VALUES (DEFAULT, ?, ?, ?);";

        insertIntoTable_order(prePreparedQuery, userId, cartStatusType.getStatusString(), totalPrice);
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

    protected void updateQuantityIn_order_product(int orderId, int existingCartItemProductId, int quantity) {
        String prePreparedQuery = "UPDATE public.order_product\n" +
                "SET product_quantity = ?\n" +
                "WHERE product_id = ? AND order_id = ?;";
        DMLPreparedQuery3Parameters(prePreparedQuery, quantity, existingCartItemProductId, orderId);

    }

    protected void addNewProductToLastCart(int newProductId) {
        String prePreparedQuery = "INSERT INTO public.order_product (id, order_id, product_id, product_quantity) " +
                "VALUES (DEFAULT, ?, ?, ?);";

        DMLPreparedQuery3Parameters(prePreparedQuery, getLastCartId(), newProductId, 1);
    }

    protected void deleteCartItemFromCart(int lastCartId, int deletedProductId) {
        String query = "DELETE FROM order_product\n" +
                "WHERE order_id = " + lastCartId + " AND product_id = " + deletedProductId + ";";
        DMLQuery(query);
    }

}
