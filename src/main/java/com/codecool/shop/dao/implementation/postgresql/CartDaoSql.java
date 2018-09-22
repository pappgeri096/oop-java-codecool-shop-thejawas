package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.util.CartStatusType;

import java.math.BigDecimal;
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
    public void add(Cart cart) {
        insertInto_order_And_order_product(cart); // TODO: DOES THIS WORK WITH AN EMPTY CART? insertEmptyCart();

    }


    @Override
    public Cart find(int id) {
        return getCartByOrder(id);
    }

    @Override
    public void remove(int id) {
        String orderProductQuery = "DELETE FROM order_product WHERE order_id ='" + id + "';";
        boolean deletionFromOrderProductSuccessful = executeInsertUpdateOrDelete(orderProductQuery);

        String orderQuery = "DELETE FROM order WHERE id ='" + id + "';";
        boolean deletionFromOrderSuccessful = executeInsertUpdateOrDelete(orderQuery);

        if (!(deletionFromOrderProductSuccessful && deletionFromOrderSuccessful)) {
            System.out.println("Unsuccessful deletion of cart with id: " + id);
        }
    }

    @Override
    public List<Cart> getAll() {
        return getAllCarts();
    }

    @Override
    public Cart getLastCart() {
        Cart currentCart;

        if (getLastCartId() == 0) {
            currentCart = new Cart();
        } else {
            currentCart = getCartByOrder(getLastCartId());
        }

        return currentCart;
    }

    @Override
    public void addToCartItemList(Product product) {
        // TODO
    }

    @Override
    public int getQuantityOfProducts() {
        // TODO
        return 0;
    }

}
