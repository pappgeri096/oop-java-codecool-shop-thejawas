package com.codecool.shop.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.util.CartStatusType;
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


@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    private static final ch.qos.logback.classic.Logger productControllerLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProductController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private final CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private final ProductDao productDataManager = IMPLEMENTATION_FACTORY.getProductDataManagerInstance();
    private final ProductCategoryDao productCategoryHandler = IMPLEMENTATION_FACTORY.getProductCategoryDataManagerInstance();
    private final SupplierDao supplierDataManager = IMPLEMENTATION_FACTORY.getSupplierDataManagerInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NullPointerException {

        String productParameter = req.getParameter("product");

        if (productParameter != null) {
            int productId = Integer.parseInt(productParameter);
            Product productToAdd = productDataManager.getBy(productId);
            // TODO: REMOVE STATUS SETTING FROM THIS METHOD. cALL UPDATE STATUS SEPARATELY
            // TODO: UPDATE TOTAL PRICE EVERY PRODUCT ADD FROM INDEX PAGE
            cartDataManager.addToLastCart(productToAdd);
            cartDataManager.updateCartStatusBy(cartDataManager.getLargestCartId(), CartStatusType.UNFINISHED);

            productControllerLogger.info("{} successfully added to cart", productToAdd.getName());

        }

        String categoryParameter = req.getParameter("category");
        String supplierParameter = req.getParameter("supplier");
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<Product> productList = productDataManager.getAll();

        if (categoryParameter != null) {
            int productCategoryId = Integer.parseInt(categoryParameter);

            if (productCategoryId > 0 && productCategoryId <= productCategoryHandler.getAll().size()) {
                productList = productDataManager.getBy(productCategoryHandler.find(productCategoryId));
            }
        } else if (supplierParameter != null) {
            int supplierId = Integer.parseInt(supplierParameter);
            if (supplierId > 0 && supplierId <= supplierDataManager.getAll().size()) {
                productList = productDataManager.getBy(supplierDataManager.find(supplierId));
            }
        }

        context.setVariable("categories", productCategoryHandler.getAll());
        context.setVariable("suppliers", supplierDataManager.getAll());
        context.setVariable("products", productList);
        context.setVariable("quantityOfProducts", cartDataManager.getQuantityOfProductsInLastCart());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
