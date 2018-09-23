package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;


public interface CartDao extends BaseDAO<Cart> {

    Cart getLastCart();
    BigDecimal getTotalPriceOfLastCart();
    BigDecimal getSubTotalPriceFromLastCartByProduct(int id);
    BigDecimal getDefaultPriceFromLastCartBy(int productId);
    BigDecimal getQuantityFromLastCartBy(int productId);
    Currency getCurrencyFromLastCartBy(int productId);

    void addToLastCart(Product product);
    int getQuantityOfProductsInLastCart();

    void saveChangesInCartAutomatically(List<CartItem> currentCartsItemList);
}
