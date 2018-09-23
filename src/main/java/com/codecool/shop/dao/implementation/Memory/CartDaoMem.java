package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.util.CartStatusType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CartDaoMem implements CartDao {
    private static CartDao instance = null;

    private List<Cart> data = new ArrayList<>();

    private CartDaoMem() {
    }

    public static CartDao getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Cart objectType) {
        objectType.setId(data.size() + 1);
        data.add(objectType);
    }

    @Override
    public Cart find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Cart> getAll() {
        return data;
    }

    @Override
    public Cart getLastCart() {
        return data.get(data.size() - 1);
    }

    @Override
    public BigDecimal getTotalPriceOfLastCart() {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            sumPrice = cartItem.getProduct().getDefaultPrice().multiply(new BigDecimal(cartItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void addToLastCart(Product product) {
        boolean wasProductFound = false;
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            if (cartItem.getProduct().getId() == (product.getId())) {
                cartItem.incrementQuantity();
                wasProductFound = true;
            }
        }
        if (!wasProductFound){
            getLastCart().getCartItemList().add(new CartItem(createIdForCartItem(), product));
        }

    }

    private int createIdForCartItem() {
        return getLastCart().getCartItemList().size() + 1;
    }

    @Override
    public int getQuantityOfProductsInLastCart() {
        int numberOfItems = 0;
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            numberOfItems += cartItem.getQuantity();
        }
        return numberOfItems;
    }

    @Override
    public void saveChangesInCartAutomatically(List<CartItem> currentCartsItemList) {
        getLastCart().setCartStatusType(CartStatusType.UNFINISHED);
        System.out.println("Cart is already saved in memory");
        System.out.println(currentCartsItemList);
    }

    @Override
    public BigDecimal getSubTotalPriceFromLastCartByProduct(int id) {
        BigDecimal defaultPrice = getDefaultPriceFromLastCartBy(id);
        BigDecimal quantity = getQuantityFromLastCartBy(id);

        BigDecimal bigDecimalSubtotal = defaultPrice.multiply(quantity);

        BigDecimal subtotal = bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
        return subtotal;
    }

    @Override
    public BigDecimal getDefaultPriceFromLastCartBy(int productId) {
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            Product product = cartItem.getProduct();
            if (product.getId() == productId) {
                return product.getDefaulPrice();
            }
        }
        return null;
    }

    @Override
    public BigDecimal getQuantityFromLastCartBy(int productId) {
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            if (cartItem.getProduct().getId() == productId) {
                return new BigDecimal(cartItem.getQuantity());
            }
        }
        return null;

    }

    @Override
    public Currency getCurrencyFromLastCartBy(int productId) {
        for (CartItem cartItem : getLastCart().getCartItemList()) {
            Product product = cartItem.getProduct();
            if (product.getId() == productId) {
                return product.getDefaultCurrency();
            }
        }
        return null;
    }

}
