package com.codecool.shop.config;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;

@WebListener
public class Initializer implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDaoMem = ProductDaoMem.getInstance();

        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();


        CartDao cartDaoMem = CartDaoMem.getInstance();
        cartDaoMem.add(new Cart());

        //setting up a new supplier
        Supplier amazon = new Supplier(1, "Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier(2, "Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        Supplier universe = new Supplier(3, "Universe", "Stars, black holes, atoms, space, energy");
        supplierDataStore.add(universe);
        Supplier earth = new Supplier(4, "Earth", "Water, breatheable air, solid land mass, energy");
        supplierDataStore.add(earth);

        Supplier ebay = new Supplier(5, "Ebay", "Everything you can imagine");
        supplierDataStore.add(ebay);


        //setting up a new product category
        ProductCategory tablet = new ProductCategory(1, "Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);

        ProductCategory energy = new ProductCategory(2, "Energy", "Esoteric", "Paranormal energies.");
        productCategoryDataStore.add(energy);

        ProductCategory bodyPillow = new ProductCategory(3, "Body Pillow", "Pillow", "Confortable Pillows.");
        productCategoryDataStore.add(bodyPillow);


        //setting up products and printing it
        productDaoMem.add(new Product(1, "Amazon Fire", BigDecimal.valueOf(49.91), "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDaoMem.add(new Product(2, "Lenovo IdeaPad Miix 700", BigDecimal.valueOf(479.11), "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDaoMem.add(new Product(3, "Amazon Fire HD 8", BigDecimal.valueOf(89.21), "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));

        productDaoMem.add(new Product(4, "Health Energy", BigDecimal.valueOf(19.99), "USD", "Coming from mother Earth, this is the most efficient energy on the market, if you want to recover from any illness or just stay healthy, our first class Health Energy is just for you.", energy, earth));
        productDaoMem.add(new Product(5, "Life Energy", BigDecimal.valueOf(99.99), "USD", "We are selling the most healthiest life energy available on Planet Earth. Get your package now, and get revitalized.", energy, universe));
        productDaoMem.add(new Product(6, "Money Energy", BigDecimal.valueOf(199.89), "USD", "You wanna get rich quick? Then, order this money energy package right now and become rich in no time. It is handy, quick to absorb and recommended by 9 out of 10 rich people", energy, earth));

        productDaoMem.add(new Product(7, "Maiden Body Pillow", BigDecimal.valueOf(80.33), "USD", "Maiden body pillow, designed to fulfill all your desires.", bodyPillow, ebay));
        productDaoMem.add(new Product(8, "Levi Body Pillow", BigDecimal.valueOf(50.76), "USD", "A body pillow of Levi, designed to fulfill all your desires.", bodyPillow, ebay));
        productDaoMem.add(new Product(9, "Sebastian Body Pillow", BigDecimal.valueOf(40.43), "USD", "A body pillow of Sebastian, designed to fulfill all your desires.", bodyPillow, ebay));
    }
}