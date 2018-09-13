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

    private Map<String, String> userDataMap = new HashMap<>(); // TODO: moves to customer
    private Map<String, Integer> productNameAndQuantityMap = new HashMap<>();
    private List<String> checkoutData = Arrays.asList("fullName", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip"); // TODO: moves to customer


    /* A private Constructor prevents any other class from instantiating.
     */
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
        data.clear();
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
    public BigDecimal getTotalPrice() {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : getCurrent().getCartItemList()) {
            sumPrice = cartItem.getProduct().getDefaultPrice().multiply(new BigDecimal(cartItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }

//    @Override
//    public void createUserDataMap(List<String> userData) { // TODO: moves to customerdao
//        for (int i = 0; i < 11; i++) {
//            this.userDataMap.put(checkoutData.get(i), userData.get(i));
//        }
//    }

//    @Override
//    public Map<String, String> getUserDataMap() {
//        return userDataMap;
//    } // TODO: moves to customerdao
//
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
}
