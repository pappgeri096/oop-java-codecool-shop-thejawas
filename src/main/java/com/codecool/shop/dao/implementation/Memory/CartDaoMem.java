package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.LineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CartDaoMem implements CartDao {
    private static CartDao instance = null;

    private List<Cart> data = new ArrayList<>();

    private Map<String, String> userDataMap = new HashMap<>();
    private Map<String, Integer> productNameAndQuantityMap = new HashMap<>();
    private List<String> checkoutData = Arrays.asList("fullName", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip");


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
    public Cart getCurrent() {
        return data.get(data.size() - 1);
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
    public BigDecimal getTotalPrice() {
        BigDecimal sumPrice = BigDecimal.valueOf(0);
        for (LineItem lineItem : getCurrent().getLineItemList()) {
            sumPrice = lineItem.getProduct().getDefaultPrice().multiply(new BigDecimal(lineItem.getQuantity())).add(sumPrice);
        }
        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public void createUserDataMap(List<String> userData) {
        for (int i = 0; i < 11; i++) {
            this.userDataMap.put(checkoutData.get(i), userData.get(i));
        }
    }

    public Map<String, String> getUserDataMap() {
        return userDataMap;
    }

    public void createProductNameAndQuantityMaps() {
        for (LineItem lineItem: getCurrent().getLineItemList()) {
            productNameAndQuantityMap.put(lineItem.getProduct().getName(), lineItem.getQuantity());
        }
    }

    public Map<String, Integer> getProductNameAndQuantityMap() {
        return productNameAndQuantityMap;
    }
}
