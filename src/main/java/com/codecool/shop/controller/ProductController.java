package com.codecool.shop.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.codecool.shop.model.BaseModel;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


import org.slf4j.LoggerFactory;

import com.codecool.shop.config.TemplateEngineUtil;

import com.codecool.shop.model.Product;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;

import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;

//import com.codecool.shop.dao.implementation.postgresql.CartDaoSql;
//import com.codecool.shop.dao.implementation.postgresql.ProductCategoryDaoSql;
//import com.codecool.shop.dao.implementation.postgresql.ProductDaoSql;
//import com.codecool.shop.dao.implementation.postgresql.SupplierDaoSql;





@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    private CartDao cartHandler = CartDaoMem.getInstance();
    private ProductDao productHandler = ProductDaoMem.getInstance();
    private ProductCategoryDao productCategoryHandler = ProductCategoryDaoMem.getInstance();
    private SupplierDao supplierHandler = SupplierDaoMem.getInstance();


//    private ProductDao productDaoSql = ProductDaoSql.getInstance();
//    private ProductCategoryDao productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
//    private SupplierDao supplierDaoSql = SupplierDaoSql.getInstance();


    private static final ch.qos.logback.classic.Logger productControllerLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Product> productList = productHandler.getAll();

        String productParameter = req.getParameter("product");

        if (productParameter != null) {
            int productId = Integer.parseInt(productParameter);
            Product productToAdd = productHandler.getBy(productId);
            cartHandler.getCurrent().addProduct(productToAdd);

            productControllerLogger.info("{} successfully added to cart", productToAdd.getName());

        }

        String categoryParameter = req.getParameter("category");
        String supplierParameter = req.getParameter("supplier");
        WebContext context = new WebContext(req, resp, req.getServletContext());

        if (categoryParameter != null) {
            int productCategoryId = Integer.parseInt(categoryParameter);

            if (productCategoryId > 0 && productCategoryId <= productCategoryHandler.getAll().size()) {
                productList = productHandler.getBy(productCategoryHandler.find(productCategoryId));
            }
        } else if (supplierParameter != null) {
            int supplierId = Integer.parseInt(supplierParameter);
            if (supplierId > 0 && supplierId <= supplierHandler.getAll().size()) {
                productList = productHandler.getBy(supplierHandler.find(supplierId));
            }
        }

        context.setVariable("categories", productCategoryHandler.getAll());
        context.setVariable("suppliers", supplierHandler.getAll());
        context.setVariable("products", productList);
        context.setVariable("orderMem", cartHandler.getCurrent());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
