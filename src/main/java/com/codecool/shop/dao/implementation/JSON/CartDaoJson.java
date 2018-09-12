package com.codecool.shop.dao.implementation.JSON;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * CartDaoJson class implements CartDao class.
 * */
public class CartDaoJson {

    private String uuidString;

    /**
     * writes single order data to json file
     * creates UUID for each order and gives the file this UUID as name
     *
     * @param objectType
     *
     * */
    public void add(Cart objectType) {
        uuidString = createUuid();
        Map<String, String> orderDataMap = joinMaps(uuidString);

        String filePathAndName = "log/json_orders/Order_" + uuidString + ".json";

        ObjectMapper orderMapper = new ObjectMapper();
        try {
            orderMapper.writeValue(new FileOutputStream(filePathAndName), orderDataMap);
            writeNewId(uuidString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    private String createUuid() {
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

    private List<String> writeNewId(String newId) {
        List<String> orderIdHistory = getAllOrderIds();
        orderIdHistory.add(newId);

        ObjectMapper orderIdWriter = new ObjectMapper();
        try {
            orderIdWriter.writeValue(new FileOutputStream("log/json_orders/OrderIdHistory.json"), orderIdHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderIdHistory;
    }

    private List<String> getAllOrderIds() {
        List<String> orderIdHistory = new ArrayList<>();
        TypeReference<List<String>> listTypeReference = new TypeReference<List<String>>(){};
        ObjectMapper orderIdReader = new ObjectMapper();
        try {
            orderIdHistory = orderIdReader.readValue(new File("log/json_orders/OrderIdHistory.json"), listTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderIdHistory;
    }


    private Map<String, String> joinMaps(String uuidString) {
        CartDao cartDaoMem = CartDaoMem.getInstance();
        Map<String, Integer> productNameAndQuantityMap = ((CartDaoMem) cartDaoMem).getProductNameAndQuantityMap();
        Map<String, String> userDataMap = ((CartDaoMem) cartDaoMem).getUserDataMap();


        Map<String, String> orderDataMap = new HashMap<>();

        for (Map.Entry<String, String> entrySet : userDataMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), entrySet.getValue());
        }


        for (Map.Entry<String, Integer> entrySet : productNameAndQuantityMap.entrySet()) {
            orderDataMap.put(entrySet.getKey(), String.valueOf(entrySet.getValue()));
        }

        orderDataMap.put("OrderId", uuidString);
        orderDataMap.put("OrderStatus", "Unshipped");

        return orderDataMap;
    }

    /**
     * Gets current order from filelist? or ???
     * */
    public String mapToJsonString(Map map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String carAsString = "";
        try {
            carAsString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return carAsString;
    }

    public String listToJsonString(List list) {
        ObjectMapper objectMapper = new ObjectMapper();

        String listAsString = "";
        try {
            listAsString = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listAsString;
    }

    public String getUuidString() {
        return uuidString;
    }


    public String orderToJsonString(Cart orderFromMemory) {
        String uuidString = createUuid();
        Map<String, String> orderDataMap = joinMaps(uuidString);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderAsString = "";
        try {
            orderAsString = objectMapper.writeValueAsString(orderDataMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return orderAsString;
    }

}
