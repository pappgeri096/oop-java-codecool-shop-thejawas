package com.codecool.shop.util.json_file_handling;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LongSummaryStatistics;

public class JsonMappingHandler {

    static void listToJsonFile(String filePath, List list) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File(filePath), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static <E> List<E> jsonFileToObjectList(String fileName, Class<?> type) {

        ObjectMapper objectMapper = new ObjectMapper();
        List<E> objectList = null;
        try {
            objectList = objectMapper.readValue(new File(fileName), new TypeReference<List<E>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }


}
