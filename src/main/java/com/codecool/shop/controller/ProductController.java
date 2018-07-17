package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        List<Product> productList = productDataStore.getAll();

        String categoryParameter = req.getParameter("category");

        Map params = new HashMap<>();
        if (categoryParameter != null) {
            int productCategoryId = Integer.parseInt(categoryParameter);

            if (productCategoryId > 0 && productCategoryId < 3) {
                productList = productDataStore.getBy(productCategoryDataStore.find(productCategoryId));
                params.put("category", productCategoryDataStore.find(productCategoryId));
            }
        }

        params.put("products", productList);
        params.put("recipient", "World");

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariables(params);
        engine.process("product/index.html", context, resp.getWriter());
    }

}
