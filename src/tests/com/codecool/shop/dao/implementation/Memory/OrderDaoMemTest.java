package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.model.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoMemTest {
    private OrderDaoMem orderDaoMem = OrderDaoMem.getInstance();


    private void addOrder(){
        Order order = new Order();
        orderDaoMem.add(order);
    }

    private Order addOrderAndReturn(){
        Order order = new Order();
        orderDaoMem.add(order);
        return order;
    }

    @Test
    void getInstanceTest() {
        OrderDaoMem orderDaoMemTest = OrderDaoMem.getInstance();
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