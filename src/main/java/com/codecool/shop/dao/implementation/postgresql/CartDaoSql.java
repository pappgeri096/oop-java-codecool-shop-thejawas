package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.*;

import com.codecool.shop.dao.implementation.postgresql.query_util.CartQueryHandler;
import com.codecool.shop.util.CartStatusType;

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
                updateQuantityAndStatusIn_order_product(getLargestCartId(), existingCartItemProductId, (cartItem.getQuantity() + 1), CartStatusType.UNFINISHED);
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
        List<CartItem> lastCartItemList = getLastCart().getCartItemList();
        int numberOfProducts = 0;
        for (CartItem cartItem : lastCartItemList) {
            numberOfProducts += cartItem.getQuantity();
        }
        return numberOfProducts;
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

                System.out.println("unsavedProductId: " + unsavedProductId);
                System.out.println("unsavedProductQuantity: " + unsavedProductQuantity);
                System.out.println("updatedProductId: " + updatedProductId);
                System.out.println("updatedProductQuantity: " + updatedProductQuantity);
                if (unsavedProductId == updatedProductId) {
                    updatedCartItemDeleted = false;
                    if (updatedProductQuantity != unsavedProductQuantity) {
                        updateQuantityAndStatusIn_order_product(getLargestCartId(), updatedProductId, updatedProductQuantity, CartStatusType.UNFINISHED);
                    }
                    break;
                }
            }

            if (updatedCartItemDeleted) {
                deleteCartItemFromCart(getLargestCartId(), unsavedProductId);
            }
        }
        System.out.println(updatedCartsItemList);
        System.out.println(unsavedCartsItemList);
    }

    // TODO: IMPLEMENT all below
    @Override
    public BigDecimal getSubTotalPriceFromLastCartByProduct(int id) {
        return null;
    }

    @Override
    public BigDecimal getDefaultPriceFromLastCartBy(int productId) {
        return null;
    }

    @Override
    public BigDecimal getQuantityFromLastCartBy(int productId) {
        return null;
    }

    @Override
    public Currency getCurrencyFromLastCartBy(int productId) {
        return null;
    }
}
