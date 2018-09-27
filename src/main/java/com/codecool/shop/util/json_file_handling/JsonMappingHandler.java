package com.codecool.shop.util.json_file_handling;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonMappingHandler {

    // From Java objects to JSON

    protected static <T> String objectToJsonString(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String carAsString = "";
        try {
            carAsString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return carAsString;
    }

    protected static <T> void objectToJsonFile(String fileName, T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new FileOutputStream(fileName), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static <E> String listToJsonString(List<E> list) {
        ObjectMapper objectMapper = new ObjectMapper();

        String listAsString = "";
        try {
            listAsString = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listAsString;
    }

    public static void listToJsonFile(String filePath, List list) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File(filePath), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static <K, V> String mapToJsonString(Map<K, V> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String carAsString = "";
        try {
            carAsString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return carAsString;
    }

    /**
     * writes cart data to json file
     * creates UUID for each order and gives the file this UUID as name
     *
     * @param filePath
     * @param mapObject
     *
     * */

    protected static <T, K, V> void writeMapToFile(String filePath, Map<K, V> mapObject) {

        ObjectMapper orderMapper = new ObjectMapper();
        try {
            orderMapper.writeValue(new FileOutputStream(filePath), mapObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JSON TO JAVA OBJECTS

    protected static <T> T jsonStringToObject(String jsonString, Class<T> className) {

        ObjectMapper objectMapper = new ObjectMapper();

        T someObject = null;
        try {
            someObject = objectMapper.readValue(jsonString, className);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return someObject;
    }

    public static <T> T jsonFileToObject(String pathName, Class<T> className) {

        ObjectMapper objectMapper = new ObjectMapper();

        T someObject = null;
        try {
            someObject = objectMapper.readValue(new File(pathName), className);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return someObject;
    }

    protected static <E> List<E> jsonArrayStringToObjectList(String jsonArray, Class<E> className) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<E> objectList = null;
        try {
            objectList = objectMapper.readValue(jsonArray, new TypeReference<List<E>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public static <E> List<E> jsonFileToObjectList(String fileName, E className) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<E> objectList = null;
        try {
            objectList = objectMapper.readValue(new File(fileName), new TypeReference<List<E>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public static List<Supplier> jsonFileToSupplierList(String fileName) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Supplier> objectList = null;
        try {
            objectList = objectMapper.readValue(new File(fileName), new TypeReference<List<Supplier>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public static List<ProductCategory> jsonFileToProductCategoryList(String fileName) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<ProductCategory> objectList = null;
        try {
            objectList = objectMapper.readValue(new File(fileName), new TypeReference<List<ProductCategory>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public static List<Product> jsonFileToProductList(String fileName) {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Product> objectList = null;
        try {
            objectList = objectMapper.readValue(new File(fileName), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

}
