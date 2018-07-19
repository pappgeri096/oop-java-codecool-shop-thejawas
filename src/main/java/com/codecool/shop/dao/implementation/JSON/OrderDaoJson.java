package com.codecool.shop.dao.implementation.JSON;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * TODO: DATA I NEED TO WRITE TO JSON FILE:
 * - Order ID: in Order.java
 * - Products name: in Order.java
 * - Number of products ordered: in Order.java
 * - User data: in Order.java
 * - Payment data: in Payment.java?????????
 * */
public class OrderDaoJson implements OrderDao {


    /**
     * writes single order data to json file
     * creates UUID for each order and gives the file this UUID as name
     *
     * @param order
     *
     * */
    @Override
    public void add(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Integer> productNameAndQuantityMap = order.getProductNameAndQuantityMap();
        Map<String, String> userDataMap = order.getUserDataMap();

        Map<String, String> orderDataMap = new HashMap<>();

        for (Map.Entry<String, String> entrySet : userDataMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), entrySet.getValue());
        }


        for (Map.Entry<String, Integer> entrySet : productNameAndQuantityMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), String.valueOf(entrySet.getValue()));
        }

        String uuidString = createUuid();
        String filePathAndName = "target/orders/Order_" + uuidString + ".json";
        try {
            objectMapper.writeValue(new FileOutputStream(filePathAndName), orderDataMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * finds file corresponding to one order based on UUID generated in the add() method
     * */
    @Override
    public Order find(int id) {
        return null;
    }

    /**
     * deletes file from server based on UUID
     * */
    @Override
    public void remove(int id) {

    }

    /**
     * Reads orders from all files and adds them to a list of orders
     * */
    @Override
    public List<Order> getAll() {
        return null;
    }

    /**
     * Gets current order from filelist? or ???
     * */
    @Override
    public Order getCurrent() {
        return null;
    }

    /**
     * Creates UUID for order and order file name
     * */
    String createUuid() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        OrderDaoJson orderDao = new OrderDaoJson();
        System.out.println(orderDao.createUuid());;
    }
}
