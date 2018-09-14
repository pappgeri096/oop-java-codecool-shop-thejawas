package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CartDaoMem implements CartDao {
    private static CartDao instance = null;

    private List<Cart> data = new ArrayList<>();

    private Map<String, Integer> productNameAndQuantityMap = new HashMap<>();

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
    public Cart getCurrent() {
        return data.get(data.size() - 1);
    }

    @Override
    public BigDecimal getTotalPriceOfCurrentCart() {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : getCurrent().getCartItemList()) {
            sumPrice = cartItem.getProduct().getDefaultPrice().multiply(new BigDecimal(cartItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void createProductNameAndQuantityMaps() {
        for (CartItem cartItem : getCurrent().getCartItemList()) {
            productNameAndQuantityMap.put(cartItem.getProduct().getName(), cartItem.getQuantity());
        }
    }

    @Override
    public Map<String, Integer> getProductNameAndQuantityMap() {
        return productNameAndQuantityMap;
    }

    @Override
    public void clearProductNameAndQuantityMap() {
        productNameAndQuantityMap.clear();
    }
}
