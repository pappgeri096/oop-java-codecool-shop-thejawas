package com.codecool.shop.config;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.*;
import com.codecool.shop.util.ImplementationType;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import com.codecool.shop.util.implementation_factory.MemoryFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;
import java.util.EnumMap;

@WebListener
public class Initializer implements ServletContextListener {

//    private static final String CONFIGURATION_PROPERTY_NAME = "implementation";
//    private static final String CONFIGURATION_IMPLEMENTATION_TYPE = "MEMORY";

    private static final ImplementationFactory IMPLEMENTATION_FACTORY;

    private static final ImplementationType CURRENT_IMPLEMENTATION = ImplementationType.MEMORY;
    private static final EnumMap<ImplementationType, ImplementationFactory> implementationFactoryEnumMap = new EnumMap<>(ImplementationType.class);

    static {
        implementationFactoryEnumMap.put(ImplementationType.MEMORY, new MemoryFactory());
//        implementationFactoryEnumMap.put(ImplementationType.DATABASE, new DatabaseFactory());

//        ConfigurationHandler.writeConfigurationProperty(CONFIGURATION_PROPERTY_NAME, CONFIGURATION_IMPLEMENTATION_TYPE);
//        String currentConfiguration = ConfigurationHandler.readConfigurationProperty(CONFIGURATION_PROPERTY_NAME);
//        CURRENT_IMPLEMENTATION = ImplementationType.valueOf(currentConfiguration);
        IMPLEMENTATION_FACTORY = implementationFactoryEnumMap.get(CURRENT_IMPLEMENTATION);
    }

    public static ImplementationFactory getImplementationFactory() {
        return IMPLEMENTATION_FACTORY;
    }


    private final CartDao cartDataManager;

    {
        cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        if (CURRENT_IMPLEMENTATION == ImplementationType.MEMORY) {
            initializeFromMemory();
        } else if (CURRENT_IMPLEMENTATION == ImplementationType.DATABASE) {

        }








    }

    private void initializeFromMemory() {
        SupplierDao supplierDataManager = SupplierDaoMem.getInstance();
        ProductCategoryDao productCategoryDataManager = ProductCategoryDaoMem.getInstance();
        ProductDao productDataManager = ProductDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier(1, "Amazon", "Digital content and services");
        supplierDataManager.add(amazon);
        Supplier lenovo = new Supplier(2, "Lenovo", "Computers");
        supplierDataManager.add(lenovo);

        Supplier universe = new Supplier(3, "Universe", "Stars, black holes, atoms, space, energy");
        supplierDataManager.add(universe);
        Supplier earth = new Supplier(4, "Earth", "Water, breatheable air, solid land mass, energy");
        supplierDataManager.add(earth);

        Supplier ebay = new Supplier(5, "Ebay", "Everything you can imagine");
        supplierDataManager.add(ebay);


        //setting up a new product category
        ProductCategory tablet = new ProductCategory(1, "Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataManager.add(tablet);

        ProductCategory energy = new ProductCategory(2, "Energy", "Esoteric", "Paranormal energies.");
        productCategoryDataManager.add(energy);

        ProductCategory bodyPillow = new ProductCategory(3, "Body Pillow", "Pillow", "Confortable Pillows.");
        productCategoryDataManager.add(bodyPillow);


        //setting up products and printing it
        productDataManager.add(new Product(1, "Amazon Fire", BigDecimal.valueOf(49.91), "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataManager.add(new Product(2, "Lenovo IdeaPad Miix 700", BigDecimal.valueOf(479.11), "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataManager.add(new Product(3, "Amazon Fire HD 8", BigDecimal.valueOf(89.21), "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));

        productDataManager.add(new Product(4, "Health Energy", BigDecimal.valueOf(19.99), "USD", "Coming from mother Earth, this is the most efficient energy on the market, if you want to recover from any illness or just stay healthy, our first class Health Energy is just for you.", energy, earth));
        productDataManager.add(new Product(5, "Life Energy", BigDecimal.valueOf(99.99), "USD", "We are selling the most healthiest life energy available on Planet Earth. Get your package now, and get revitalized.", energy, universe));
        productDataManager.add(new Product(6, "Money Energy", BigDecimal.valueOf(199.89), "USD", "You wanna get rich quick? Then, order this money energy package right now and become rich in no time. It is handy, quick to absorb and recommended by 9 out of 10 rich people", energy, earth));

        productDataManager.add(new Product(7, "Maiden Body Pillow", BigDecimal.valueOf(80.33), "USD", "Maiden body pillow, designed to fulfill all your desires.", bodyPillow, ebay));
        productDataManager.add(new Product(8, "Levi Body Pillow", BigDecimal.valueOf(50.76), "USD", "A body pillow of Levi, designed to fulfill all your desires.", bodyPillow, ebay));
        productDataManager.add(new Product(9, "Sebastian Body Pillow", BigDecimal.valueOf(40.43), "USD", "A body pillow of Sebastian, designed to fulfill all your desires.", bodyPillow, ebay));

        // setting up a guest user until registration and adding it to Customer data manager
        Customer guest = new Customer(1, "Guest", "jawas@jawas.hu", 702225555, "Hungary", "Budapest", "1133", "Blaha Lujza tér 1.", "Hungary", "Budapest", "1133", "Blaha Lujza tér 1.");
        CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();
        customerDataManager.add(guest);

        // setting up a new shopping cart and adding it to its data manager:
        cartDataManager.add(new Cart());


    }
}