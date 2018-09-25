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

    private static final int GUEST_ID = 1;

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
    public int getGuestId() {
        return GUEST_ID;
    }

    @Override
    public int generateIdForNewCart() {
        return data.size() + 1;
    }

    @Override
    public void add(Cart objectType) {
//        objectType.setId(data.size() + 1);
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
        return getTotalPriceBy(getLargestCartId());
    }

    public BigDecimal getTotalPriceBy(int cartId) {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : find(cartId).getCartItemList()) {
            sumPrice = cartItem.getProduct().getDefaultPrice().multiply(new BigDecimal(cartItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public void addToLastCart(Product product, CartStatusType status) {
        addProductToCartBy(getLargestCartId(), product, status);
    }

    @Override
    public void addProductToCartBy(int cartId, Product newProduct, CartStatusType status) {
        boolean wasProductFound = false;
        for (CartItem cartItem : find(cartId).getCartItemList()) {
            if (cartItem.getProduct().getId() == (newProduct.getId())) {
                cartItem.incrementQuantity();
                wasProductFound = true;
            }
        }
        if (!wasProductFound){
            find(cartId).getCartItemList().add(new CartItem(createIdForCartItem(), newProduct));
        }
        updateCartStatusBy(cartId, status);

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
    }

    @Override
    public BigDecimal getSubTotalPriceFromLastCartBy(int productId) {
        BigDecimal defaultPrice = getDefaultPriceFromLastCartBy(productId);
        BigDecimal quantity = getQuantityFromLastCartBy(productId);

        BigDecimal bigDecimalSubtotal = defaultPrice.multiply(quantity);

        return bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
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

    @Override
    public void updateCartStatusBy(int cartId, CartStatusType status) {
        find(cartId).setCartStatusType(status);
    }

    @Override
    public void updateLastCartStatus(CartStatusType status) {
        getLastCart().setCartStatusType(status);
    }

    @Override
    public int getLargestCartId() {
        return getLastCart().getId();
    }
}
