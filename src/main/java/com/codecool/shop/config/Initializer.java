package com.codecool.shop.config;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.model.order_model.OrderFromMemory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        OrderDao orderDaoMem = OrderDaoMem.getInstance();
        orderDaoMem.add(new OrderFromMemory());
    }
}
