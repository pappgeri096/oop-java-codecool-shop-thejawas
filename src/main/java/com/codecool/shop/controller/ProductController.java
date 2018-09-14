package com.codecool.shop.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.codecool.shop.config.Initializer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


import org.slf4j.LoggerFactory;

import com.codecool.shop.config.TemplateEngineUtil;

import com.codecool.shop.model.Product;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;

import com.codecool.shop.util.implementation_factory.ImplementationFactory;

import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;

import com.codecool.shop.dao.implementation.postgresql.CartDaoSql;
import com.codecool.shop.dao.implementation.postgresql.ProductCategoryDaoSql;
import com.codecool.shop.dao.implementation.postgresql.ProductDaoSql;
import com.codecool.shop.dao.implementation.postgresql.SupplierDaoSql;


@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    private static final ch.qos.logback.classic.Logger productControllerLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProductController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

//    private CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
//    private ProductDao productDataManager = IMPLEMENTATION_FACTORY.getProductDataManagerInstance();
//    private ProductCategoryDao productCategoryHandler = IMPLEMENTATION_FACTORY.getProductCategoryDataManagerInstance();
//    private SupplierDao supplierDataManager = IMPLEMENTATION_FACTORY.getSupplierDataManagerInstance();

    private CartDao cartDataManagerS = CartDaoSql.getInstance();
    private ProductDao productDataManagerS = ProductDaoSql.getInstance();
    private ProductCategoryDao productCategoryHandlerS = ProductCategoryDaoSql.getInstance();
    private SupplierDao supplierDataManagerS = SupplierDaoSql.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Product> productList = productDataManagerS.getAll();

        String productParameter = req.getParameter("product");

        if (productParameter != null) {
            int productId = Integer.parseInt(productParameter);
            Product productToAdd = productDataManagerS.getBy(productId);
            cartDataManagerS.getCurrent().addProduct(productToAdd);

            productControllerLogger.info("{} successfully added to cart", productToAdd.getName());

        }

        String categoryParameter = req.getParameter("category");
        String supplierParameter = req.getParameter("supplier");
        WebContext context = new WebContext(req, resp, req.getServletContext());

        if (categoryParameter != null) {
            int productCategoryId = Integer.parseInt(categoryParameter);

            if (productCategoryId > 0 && productCategoryId <= productCategoryHandlerS.getAll().size()) {
                productList = productDataManagerS.getBy(productCategoryHandlerS.find(productCategoryId));
            }
        } else if (supplierParameter != null) {
            int supplierId = Integer.parseInt(supplierParameter);
            if (supplierId > 0 && supplierId <= supplierDataManagerS.getAll().size()) {
                productList = productDataManagerS.getBy(supplierDataManagerS.find(supplierId));
            }
        }

        context.setVariable("categories", productCategoryHandlerS.getAll());
        context.setVariable("suppliers", supplierDataManagerS.getAll());
        context.setVariable("products", productList);
        context.setVariable("orderMem", cartDataManagerS.getCurrent());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
