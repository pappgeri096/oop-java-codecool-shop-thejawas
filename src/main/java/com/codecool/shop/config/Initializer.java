package com.codecool.shop.config;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.*;
import com.codecool.shop.util.CartStatusType;
import com.codecool.shop.util.ConfigurationHandler;
import com.codecool.shop.util.ImplementationType;
import com.codecool.shop.util.implementation_factory.DatabaseFactory;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import com.codecool.shop.util.implementation_factory.MemoryFactory;
import com.codecool.shop.util.json_file_handling.JsonMappingHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.EnumMap;
import java.util.List;

@WebListener
public class Initializer implements ServletContextListener {

    private static final String CONFIGURATION_PROPERTY_NAME = "implementation";

    private static final ImplementationType CURRENT_IMPLEMENTATION;
    private static final EnumMap<ImplementationType, ImplementationFactory> implementationFactoryEnumMap = new EnumMap<>(ImplementationType.class);

    private static final ImplementationFactory IMPLEMENTATION_FACTORY;

    static {
        implementationFactoryEnumMap.put(ImplementationType.MEMORY, new MemoryFactory());
        implementationFactoryEnumMap.put(ImplementationType.DATABASE, new DatabaseFactory());

        String currentConfiguration = ConfigurationHandler.readConfigurationProperty(CONFIGURATION_PROPERTY_NAME);
        CURRENT_IMPLEMENTATION = ImplementationType.valueOf(currentConfiguration);

        try {
            IMPLEMENTATION_FACTORY = implementationFactoryEnumMap.get(CURRENT_IMPLEMENTATION);
        } catch (NullPointerException npe) {
            System.out.println("No such implementation: " + currentConfiguration);
            npe.printStackTrace();
            throw new NullPointerException(npe.getMessage());
        }

    }

    public static ImplementationFactory getImplementationFactory() {
        return IMPLEMENTATION_FACTORY;
    }


    private final CartDao CART_DATA_MANAGER = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private final CustomerDao CUSTOMER_DATA_MANAGER = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        if (CURRENT_IMPLEMENTATION == ImplementationType.MEMORY) {
            initializeFromMemory();
            System.out.println("Now, data is stored in memory");
        } else if (CURRENT_IMPLEMENTATION == ImplementationType.DATABASE) {
            initializeFromDatabase();
            System.out.println("Now, running with data persistence");
        }
        

    }

    private void initializeFromDatabase() {
        // deleting unused empty carts:
        CART_DATA_MANAGER.remove(CartStatusType.EMPTY);
        // setting up a new shopping cart and adding it to its data manager:
        CART_DATA_MANAGER.add(new Cart(CART_DATA_MANAGER.generateIdForNewCart(), CUSTOMER_DATA_MANAGER.getGuestId(), CartStatusType.EMPTY));
    }

    private void initializeFromMemory() {

        // Initializing model data from json files
        //      Suppliers
        String filePathForSupplierData = "src/main/resources/json_data_persistence/supplier_data.json";
        loadSuppliersIntoMemory(filePathForSupplierData);

        //      Product categories
        String filePathForProductCategoryData = "src/main/resources/json_data_persistence/product_category_data.json";
        loadProductCategoriesIntoMemory(filePathForProductCategoryData);

        //      Products
        String filePathForProductData = "src/main/resources/json_data_persistence/product_data.json";
        loadProductsIntoMemory(filePathForProductData);

        // setting up a guest user until registration and adding it to Customer data manager
        Customer guest = new Customer(CUSTOMER_DATA_MANAGER.generateIdForNewCustomer(), "Guest");
        CUSTOMER_DATA_MANAGER.add(guest);

        // setting up a new shopping cart and adding it to its data manager:
        CART_DATA_MANAGER.add(new Cart(CART_DATA_MANAGER.generateIdForNewCart(), guest.getId(), CartStatusType.EMPTY));

    }

    private void loadSuppliersIntoMemory(String filePath) {
        List<Supplier> supplierList = JsonMappingHandler.jsonFileToSupplierList(filePath);
        SupplierDaoMem supplierDataManager = SupplierDaoMem.getInstance();
        supplierDataManager.setData(supplierList);

        System.out.println("suppliers:\n" + supplierList);
    }

    private void loadProductCategoriesIntoMemory(String filePathForProductCategoryData) {
        ProductCategoryDaoMem productCategoryDataManager = ProductCategoryDaoMem.getInstance();
        List<ProductCategory> productCategoryList = JsonMappingHandler.jsonFileToProductCategoryList(filePathForProductCategoryData);
        productCategoryDataManager.setData(productCategoryList);
        System.out.println("product categories:\n" + productCategoryList);
    }

    private void loadProductsIntoMemory(String filePath) {
        ProductDaoMem productDataManager = ProductDaoMem.getInstance();
        List<Product> productList = JsonMappingHandler.jsonFileToProductList(filePath);
        productDataManager.setData(productList);
        System.out.println("products:\n" + productList);
    }


}