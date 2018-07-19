package com.codecool.shop.dao.implementation.JSON;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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
        String uuidString = createUuid();
        Map<String, String> orderDataMap = joinMaps(order, uuidString);

        String filePathAndName = "target/orders/Order_" + uuidString + ".json";

        ObjectMapper orderMapper = new ObjectMapper();
        try {
            orderMapper.writeValue(new FileOutputStream(filePathAndName), orderDataMap);
            writeNewId(uuidString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ths method is executed");

    }

    List<String> writeNewId(String newId) {
        List<String> orderIdHistory = getAllOrderId();
        orderIdHistory.add(newId);

        ObjectMapper orderIdWriter = new ObjectMapper();
        try {
            orderIdWriter.writeValue(new FileOutputStream("target/orders/OrderIdHistory.json"), orderIdHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderIdHistory;
    }

    List<String> getAllOrderId() {
        List<String> orderIdHistory = new ArrayList<>();
        TypeReference<List<String>> listTypeReference = new TypeReference<List<String>>(){};
        ObjectMapper orderIdReader = new ObjectMapper();
        try {
            orderIdHistory = orderIdReader.readValue(new File("target/orders/OrderIdHistory.json"), listTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderIdHistory;
    }


    Map<String, String> joinMaps(Order order, String uuidString) {
        Map<String, Integer> productNameAndQuantityMap = order.getProductNameAndQuantityMap();
        Map<String, String> userDataMap = order.getUserDataMap();


        Map<String, String> orderDataMap = new HashMap<>();

        for (Map.Entry<String, String> entrySet : userDataMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), entrySet.getValue());
        }


        for (Map.Entry<String, Integer> entrySet : productNameAndQuantityMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), String.valueOf(entrySet.getValue()));
        }

        orderDataMap.put("OrderId", uuidString);
        orderDataMap.put("OrderStatus", "PendingUnshippedCancelled");

        return orderDataMap;
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
        List<String> allOrderIds = getAllOrderId();
        System.out.println(allOrderIds);
        boolean exists = false;
        String newUuid;
        do {
            newUuid = UUID.randomUUID().toString();
            for (String orderId :
                    allOrderIds) {
                if (orderId.equals(newUuid)) {
                    exists = true;
                    System.out.println("UUID already exists");
                    break;
                }
            }
        } while (exists);

        return newUuid;
    }

    public static void main(String[] args) {
        OrderDaoJson orderDao = new OrderDaoJson();
        System.out.println("");
    }
}
