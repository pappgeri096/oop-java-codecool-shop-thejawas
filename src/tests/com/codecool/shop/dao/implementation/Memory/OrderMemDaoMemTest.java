package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderMemDaoMemTest {
    private CartDao cartDaoMem = CartDaoMem.getInstance();


    private void addOrder(){
        Cart orderFromMemory = new Cart();
        cartDaoMem.add(orderFromMemory);
    }

    private Cart addOrderAndReturn(){
        Cart orderFromMemory = new Cart();
        cartDaoMem.add(orderFromMemory);
        return orderFromMemory;
    }

    @Test
    void getInstanceTest() {
        CartDao cartDaoMemTest = CartDaoMem.getInstance();
        assertNotNull(cartDaoMemTest);
    }

    @Test
    void getCurrentTest() {
        addOrder();
        assertNotNull(cartDaoMem.getLastCart());
    }

    @Test
    void addTest() {
        assertEquals(addOrderAndReturn(), cartDaoMem.getLastCart());
    }

    @Test
    void findTest() {
        assertEquals(addOrderAndReturn(), cartDaoMem.find(cartDaoMem.getAll().size()));
    }

    @Test
    void removeTest() {
        int size = cartDaoMem.getAll().size();
        addOrder();
        cartDaoMem.remove(1);
        assertEquals(size, cartDaoMem.getAll().size());
    }
}