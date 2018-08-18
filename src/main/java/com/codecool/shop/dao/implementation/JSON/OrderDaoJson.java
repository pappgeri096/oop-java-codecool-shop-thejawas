package com.codecool.shop.dao.implementation.JSON;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.dao.implementation.postgresql.ProductCategoryDaoSql;
import com.codecool.shop.dao.implementation.postgresql.ProductDaoSql;
import com.codecool.shop.dao.implementation.postgresql.SupplierDaoSql;
import com.codecool.shop.model.Order;

import com.codecool.shop.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * OrderDaoJson class implements OrderDao class.
 * */
public class OrderDaoJson implements OrderDao {

    String uuidString;

    /**
     * writes single order data to json file
     * creates UUID for each order and gives the file this UUID as name
     *
     * @param objectType
     *
     * */
    @Override
    public void add(Order objectType) {
        uuidString = createUuid();
        Map<String, String> orderDataMap = joinMaps(objectType, uuidString);

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
    @Override
    public Order find(int id) {
        return null;
    }

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

    /**
     * Reads orders from all files and adds them to a list of orders
     * */
    @Override
    public List<Order> getAll() {
        return null;
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
            orderIdWriter.writeValue(new FileOutputStream("log/json_orders/OrderIdHistory.json"), orderIdHistory);
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
            orderIdHistory = orderIdReader.readValue(new File("log/json_orders/OrderIdHistory.json"), listTypeReference);
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
        orderDataMap.put("OrderStatus", "Unshipped");

        return orderDataMap;
    }

    /**
     * Gets current order from filelist? or ???
     * */
    @Override
    public Order getCurrent() {
        return null;
    }

    /**
     * deletes file from server based on UUID
     * */
    @Override
    public void remove(int id) {

    }

    public String mapToJsonString(Map map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String mapAsString = "";
        try {
            mapAsString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mapAsString;
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


    public String serializeOrder(Order order) {
        String uuidString = createUuid();
        Map<String, String> orderDataMap = joinMaps(order, uuidString);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderAsString = "";
        try {
            orderAsString = objectMapper.writeValueAsString(orderDataMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return orderAsString;
    }

    public String serializeProduct(Product product) {
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedProduct = "";
        try {
            serializedProduct = objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return serializedProduct;
    }


    public String serializeProductList() {

        ProductDaoSql productDaoSql = ProductDaoSql.getSingletonInstance();
        List<Product> productList = productDaoSql.getAll();

        StringBuilder stringBuilder = new StringBuilder();

        for (Product product : productList) {
            String serializedProduct = serializeProduct(product);
            stringBuilder.append(serializedProduct);
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        ProductCategoryDao productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
        productCategoryDaoMem.setData(productCategoryDaoSql.getAll());


        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();
        SupplierDao supplierDaoSql = SupplierDaoSql.getInstance();
        supplierDaoMem.setData(supplierDaoSql.getAll());

        ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
        ProductDao productDaoSql = ProductDaoSql.getSingletonInstance();
        productDaoMem.setData(productDaoSql.getAll());

        OrderDaoJson testJSON = new OrderDaoJson();
        System.out.println(testJSON.serializeProductList());
    }

}
