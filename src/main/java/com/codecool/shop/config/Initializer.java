package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.Order;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        orderDataStore.add(new Order());

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        Supplier universe = new Supplier("Universe", "Stars, black holes, atoms, space, energy");
        supplierDataStore.add(universe);
        Supplier earth = new Supplier("Earth", "Water, breatheable air, solid land mass, energy");
        supplierDataStore.add(earth);

        Supplier ebay = new Supplier("Ebay", "Everything you can imagine");
        supplierDataStore.add(ebay);


        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);

        ProductCategory energy = new ProductCategory("Energy", "Esoteric", "Paranormal energies.");
        productCategoryDataStore.add(energy);

        ProductCategory bodyPillow = new ProductCategory("bodyPillow", "Pillow", "Confortable Pillows.");
        productCategoryDataStore.add(bodyPillow);


        //setting up products and printing it
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));

        productDataStore.add(new Product("Health Energy", 19.99f, "USD", "Coming from mother Earth, this is the most efficient energy on the market, if you want to recover from any illness or just stay healthy, our first class Health Energy is just for you.", energy, earth));
        productDataStore.add(new Product("Life Energy", 99.99f, "USD", "We are selling the most healthiest life energy available on Planet Earth. Get your package now, and get revitalized.", energy, universe));
        productDataStore.add(new Product("Money Energy", 199.89f, "USD", "You wanna get rich quick? Then, order this money energy package right now and become rich in no time. It is handy, quick to absorb and recommended by 9 out of 10 rich people", energy, earth));

        productDataStore.add(new Product("Maiden Body Pillow", 80, "USD", "Maiden body pillow, designed to fulfill all your desires.", bodyPillow, ebay));
        productDataStore.add(new Product("Levi Body Pillow", 50, "USD", "A body pillow of Levi, designed to fulfill all your desires.", bodyPillow, ebay));
        productDataStore.add(new Product("Sebastian Body Pillow", 40, "USD", "A body pillow of Sebastian, designed to fulfill all your desires.", bodyPillow, ebay));
    }
}
