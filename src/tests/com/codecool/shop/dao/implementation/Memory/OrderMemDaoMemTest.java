package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.order_model.OrderFromMemory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderMemDaoMemTest {
    private OrderDao orderDaoMem = OrderDaoMem.getInstance();


    private void addOrder(){
        OrderFromMemory orderFromMemory = new OrderFromMemory();
        orderDaoMem.add(orderFromMemory);
    }

    private OrderFromMemory addOrderAndReturn(){
        OrderFromMemory orderFromMemory = new OrderFromMemory();
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