package com.codecool.shop.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class OrderDaoJson extends JsonMappingHandler {



    public <K, V> Map<K, V> find(String id, Class<K> keyType, Class<V> valueType) {

        StringBuilder sb = new StringBuilder();
        sb.append("target/orders/").append("Order_").append(id).append(".json");

        Map<K, V> orderMap = new HashMap<>();
        TypeReference<Map<K, V>> listTypeReference = new TypeReference<Map<K, V>>(){};
        ObjectMapper orderReader = new ObjectMapper();
        try {
            orderMap = orderReader.readValue(new File(sb.toString()), listTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderMap;
    }

    /**
     Reads orders from all files and adds them to a list of orders
     */

    /**
     * finds file corresponding to one order based on UUID generated in the add() method
     * */
    public Map<String, String> find(String id) {

        StringBuilder sb = new StringBuilder();
        sb.append("target/orders/").append("Order_").append(id).append(".json");

        Map<String, String> orderMap = new HashMap<>();
        TypeReference<Map<String, String>> listTypeReference = new TypeReference<Map<String, String>>(){};
        ObjectMapper orderReader = new ObjectMapper();
        try {
            orderMap = orderReader.readValue(new File(sb.toString()), listTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderMap;
    }


    public List<Map<String, String>> getAllOrders() {
        List<Map<String, String>> allOrderData = new ArrayList<>();
        List<String> allOrderIds = getAllOrderIds();
        for (String orderId : allOrderIds) {
            allOrderData.add(find(orderId));
        }
        return allOrderData;
    }

    /**
     * Creates UUID for order and order file name
     * */
    String createUuid() {
        List<String> allOrderIds = getAllOrderIds();
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

    List<String> writeNewId(String newId) {
        List<String> orderIdHistory = getAllOrderIds();
        orderIdHistory.add(newId);

        ObjectMapper orderIdWriter = new ObjectMapper();
        try {
            orderIdWriter.writeValue(new FileOutputStream("target/orders/OrderIdHistory.json"), orderIdHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderIdHistory;
    }

    List<String> getAllOrderIds() {
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

}
