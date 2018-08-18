package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.dao.implementation.postgresql.ProductCategoryDaoSql;
import com.codecool.shop.dao.implementation.postgresql.ProductDaoSql;
import com.codecool.shop.dao.implementation.postgresql.SupplierDaoSql;
import com.codecool.shop.model.Order;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    /**
     * TODO: INITIALIZE MODEL OBJECTS BY LOADING DATA FROM DATABASE TO CORRESPONDING LISTS OF MODEL OBJECTS
     * */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        ProductCategoryDao productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
        productCategoryDaoMem.setData(productCategoryDaoSql.getAll());

        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();
        SupplierDao supplierDaoSql = SupplierDaoSql.getInstance();
        supplierDaoMem.setData(supplierDaoSql.getAll());

        ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
        ProductDao productDaoSql = ProductDaoSql.getSingletonInstance();
        productDaoMem.setData(productDaoSql.getAll());

        OrderDao orderDaoMem = OrderDaoMem.getInstance();
        orderDaoMem.add(new Order());
    }
}
