package com.codecool.shop.util.json_file_handling;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import com.codecool.shop.util.implementation_factory.MemoryFactory;

import java.math.BigDecimal;
import java.util.*;

class WriteOriginalModelDataToJsonFiles extends JsonMappingHandler {

    private static final ImplementationFactory MEMORY_FACTORY = new MemoryFactory();
//    private static final CartDao CART_DATA_MANAGER = MEMORY_FACTORY.getCartDataManagerInstance();
//    private static final CustomerDao CUSTOMER_DATA_MANAGER = MEMORY_FACTORY.getCustomerDataManagerInstance();

    private static final SupplierDao SUPPLIER_DATA_MANAGER = SupplierDaoMem.getInstance();
    private static final ProductCategoryDao PRODUCT_CATEGORY_DATA_MANAGER = ProductCategoryDaoMem.getInstance();
    private static final ProductDao PRODUCT_DATA_MANAGER = ProductDaoMem.getInstance();

    //setting up a new supplier
    private static Supplier amazon = new Supplier(1, "Amazon", "Digital content and services");
    private static Supplier lenovo = new Supplier(2, "Lenovo", "Computers");
    private static Supplier universe = new Supplier(3, "Universe", "Stars, black holes, atoms, space, energy");
    private static Supplier earth = new Supplier(4, "Earth", "Water, breatheable air, solid land mass, energy");
    private static Supplier ebay = new Supplier(5, "Ebay", "Everything you can imagine");

    //setting up a new product category
    private static ProductCategory tablet = new ProductCategory(1, "Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
    private static ProductCategory energy = new ProductCategory(2, "Energy", "Esoteric", "Paranormal energies.");
    private static ProductCategory bodyPillow = new ProductCategory(3, "Body Pillow", "Pillow", "Confortable Pillows.");


    static {
        SUPPLIER_DATA_MANAGER.add(amazon);
        SUPPLIER_DATA_MANAGER.add(lenovo);
        SUPPLIER_DATA_MANAGER.add(ebay);
        SUPPLIER_DATA_MANAGER.add(universe);
        SUPPLIER_DATA_MANAGER.add(earth);

        PRODUCT_CATEGORY_DATA_MANAGER.add(tablet);
        PRODUCT_CATEGORY_DATA_MANAGER.add(energy);
        PRODUCT_CATEGORY_DATA_MANAGER.add(bodyPillow);

        //setting up products and printing it
        PRODUCT_DATA_MANAGER.add(new Product(1, "Amazon Fire",BigDecimal.valueOf(49.91), "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        PRODUCT_DATA_MANAGER.add(new Product(2, "Lenovo IdeaPad Miix 700", BigDecimal.valueOf(479.11), "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        PRODUCT_DATA_MANAGER.add(new Product(3, "Amazon Fire HD 8", BigDecimal.valueOf(89.21), "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));

        PRODUCT_DATA_MANAGER.add(new Product(4, "Health Energy", BigDecimal.valueOf(19.99), "USD", "Coming from mother Earth, this is the most efficient energy on the market, if you want to recover from any illness or just stay healthy, our first class Health Energy is just for you.", energy, earth));
        PRODUCT_DATA_MANAGER.add(new Product(5, "Life Energy", BigDecimal.valueOf(99.99), "USD", "We are selling the most healthiest life energy available on Planet Earth. Get your package now, and get revitalized.", energy, universe));
        PRODUCT_DATA_MANAGER.add(new Product(6, "Money Energy", BigDecimal.valueOf(199.89), "USD", "You wanna get rich quick? Then, order this money energy package right now and become rich in no time. It is handy, quick to absorb and recommended by 9 out of 10 rich people", energy, earth));

        PRODUCT_DATA_MANAGER.add(new Product(7, "Maiden Body Pillow", BigDecimal.valueOf(80.33), "USD", "Maiden body pillow, designed to fulfill all your desires.", bodyPillow, ebay));
        PRODUCT_DATA_MANAGER.add(new Product(8, "Levi Body Pillow", BigDecimal.valueOf(50.76), "USD", "A body pillow of Levi, designed to fulfill all your desires.", bodyPillow, ebay));
        PRODUCT_DATA_MANAGER.add(new Product(9, "Sebastian Body Pillow", BigDecimal.valueOf(40.43), "USD", "A body pillow of Sebastian, designed to fulfill all your desires.", bodyPillow, ebay));

    }

    // TODO: Run this only to re-save original data

    public static void main(String[] args) {

        String filePathForProductData = "src/main/resources/json_data_persistence/product_data.json";
        String filePathForProductCategoryData = "src/main/resources/json_data_persistence/product_category_data.json";
        String filePathForSupplierData = "src/main/resources/json_data_persistence/supplier_data.json";

//        saveProducts(filePathForProductData);
//        saveProductCategoriesNoProductList(filePathForProductCategoryData);
//        saveSuppliersNoProductList(filePathForSupplierData);

    }

    private static void saveProducts(String filePath) {
        listToJsonFile(filePath, PRODUCT_DATA_MANAGER.getAll());
    }

    private static void saveProductCategoriesNoProductList(String filePath) {
        List<ProductCategory> productCategoryList = PRODUCT_CATEGORY_DATA_MANAGER.getAll();
        listToJsonFile(filePath, productCategoryList);

    }

    private static void saveSuppliersNoProductList(String filePath) {
        List<Supplier> supplierList = SUPPLIER_DATA_MANAGER.getAll();
        listToJsonFile(filePath, supplierList);

    }
}
