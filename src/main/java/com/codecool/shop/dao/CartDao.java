package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.util.CartStatusType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;


public interface CartDao extends BaseDAO<Cart> {

    Cart getLastCart();
    BigDecimal getTotalPriceOfLastCart();
    BigDecimal getSubTotalPriceFromLastCartBy(int productId);
    BigDecimal getDefaultPriceFromLastCartBy(int productId);
    BigDecimal getQuantityFromLastCartBy(int productId);
    Currency getCurrencyFromLastCartBy(int productId);

    void addToLastCart(Product product, CartStatusType status);

    void addProductToCart(int cartId, Product newProduct, CartStatusType status);

    int getQuantityOfProductsInLastCart();

    void saveChangesInCartAutomatically(List<CartItem> currentCartsItemList);

    void updateCartStatusBy(int cartId, CartStatusType status);
    void updateLastCartStatus(CartStatusType status);
    int getLargestCartId();

    int generateIdForNewCart();
    int getGuestId();
}
