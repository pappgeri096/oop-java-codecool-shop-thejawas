package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.WsOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderMemDaoMemTest {
    private OrderDao orderDaoMem = OrderDaoMem.getInstance();


    private void addOrder(){
        WsOrder orderFromMemory = new WsOrder();
        orderDaoMem.add(orderFromMemory);
    }

    private WsOrder addOrderAndReturn(){
        WsOrder orderFromMemory = new WsOrder();
        orderDaoMem.add(orderFromMemory);
        return orderFromMemory;
    }

    @Test
    void getInstanceTest() {
        OrderDao orderDaoMemTest = OrderDaoMem.getInstance();
        assertNotNull(orderDaoMemTest);
    }

    @Test
    void getCurrentTest() {
        addOrder();
        assertNotNull(orderDaoMem.getCurrent());
    }

    @Test
    void addTest() {
        assertEquals(addOrderAndReturn(),orderDaoMem.getCurrent());
    }

    @Test
    void findTest() {
        assertEquals(addOrderAndReturn(),orderDaoMem.find(orderDaoMem.getAll().size()));
    }

    @Test
    void removeTest() {
        int size = orderDaoMem.getAll().size();
        addOrder();
        orderDaoMem.remove(1);
        assertEquals(size,orderDaoMem.getAll().size());
    }
}