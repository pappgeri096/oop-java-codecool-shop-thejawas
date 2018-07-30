package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.postgresql.ProductDaoSql;
import org.slf4j.LoggerFactory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    private static final ch.qos.logback.classic.Logger productControllerLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductDao productDatabaseStorage = ProductDaoSql.getSingletonInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        List<Product> productList = productDataStore.getAll();

        String categoryParameter = req.getParameter("category");

        String productParameter = req.getParameter("product");

        if (productParameter != null) {
            int productId = Integer.parseInt(productParameter);
            Product productToAdd = productDataStore.getBy(productId);
            orderDataStore.getCurrent().addProduct(productToAdd);

            productControllerLogger.info("{} successfully added to cart", productToAdd.getName());

        }
        String supplierParameter = req.getParameter("supplier");
        WebContext context = new WebContext(req, resp, req.getServletContext());
        if (categoryParameter != null) {
            int productCategoryId = Integer.parseInt(categoryParameter);

            if (productCategoryId > 0 && productCategoryId <= productCategoryDataStore.getAll().size()) {
                productList = productDataStore.getBy(productCategoryDataStore.find(productCategoryId));
            }
        } else if (supplierParameter != null) {
            int supplierId = Integer.parseInt(supplierParameter);
            if (supplierId > 0 && supplierId <= supplierDataStore.getAll().size()) {
                productList = productDataStore.getBy(supplierDataStore.find(supplierId));
            }
        }

        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("products", productList);
        context.setVariable("order", orderDataStore.getCurrent());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
