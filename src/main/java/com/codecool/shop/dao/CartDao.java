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
    int getLargestCartId();
    BigDecimal getSubTotalPriceFromLastCartBy(int productId);
    BigDecimal getDefaultPriceFromLastCartBy(int productId);
    BigDecimal getQuantityFromLastCartBy(int productId);
    Currency getCurrencyFromLastCartBy(int productId);
    int getQuantityOfProductsInLastCart();

    BigDecimal getTotalPriceOfLastCart();
    BigDecimal getTotalPriceBy(int cartId);

    void addToLastCart(Product product);
    void addProductToCartBy(int cartId, Product newProduct);

    void saveChangesInCartAutomatically(List<CartItem> currentCartsItemList);

    void updateCartStatusBy(int cartId, CartStatusType status);
    void updateLastCartStatus(CartStatusType status);

    int generateIdForNewCart();
}
