package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;

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
        boolean deletionFromOrderProductSuccessful = DMLQuery(orderProductQuery);

        String orderQuery = "DELETE FROM order WHERE id ='" + id + "';";
        boolean deletionFromOrderSuccessful = DMLQuery(orderQuery);

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
        Cart lastCart;

        if (getLargestCartId() == 0) {
            lastCart = new Cart(getLargestCartId() + 1);
        } else {
            lastCart = getCartByOrder(getLargestCartId());
        }

        return lastCart;
    }

    @Override
    public void addToLastCart(Product newProduct) {
        int newProductId = newProduct.getId();
        boolean productNotInCart = true;
        for (CartItem cartItem: getLastCart().getCartItemList()) {
            int existingCartItemProductId = cartItem.getProduct().getId();
            if (newProductId == existingCartItemProductId) {
                updateQuantityIn_order_product(getLargestCartId(), existingCartItemProductId, cartItem.getQuantity() + 1);
                productNotInCart = false;
                break;
            }
        }
        if (productNotInCart) {
            addNewProductToLastCart(newProductId);
        }
        getLargestCartId();
    }

    @Override
    public int getQuantityOfProductsInLastCart() {
        return getLastCart().getCartItemList().size();
    }

    @Override
    public void saveChangesInCartAutomatically(List<CartItem> updatedCartsItemList) {
        List<CartItem> unsavedCartsItemList = getLastCart().getCartItemList();
        for (CartItem unsavedCartItem : unsavedCartsItemList) {
            int unsavedProductId = unsavedCartItem.getProduct().getId();
            int unsavedProductQuantity = unsavedCartItem.getQuantity();

            boolean updatedCartItemDeleted = true;
            for (CartItem updatedCartItem: updatedCartsItemList) {

                int updatedProductId = updatedCartItem.getProduct().getId();
                int updatedProductQuantity = updatedCartItem.getQuantity();

                if (unsavedProductId == updatedProductId) {
                    updatedCartItemDeleted = false;
                    if (updatedProductQuantity != unsavedProductQuantity) {
                        updateQuantityIn_order_product(getLargestCartId(), updatedProductId, updatedProductQuantity);
                    }
                    break;
                }
            }

            if (updatedCartItemDeleted) {
                deleteCartItemFromCart(getLargestCartId(), unsavedProductId);
            }
        }
    }
}
