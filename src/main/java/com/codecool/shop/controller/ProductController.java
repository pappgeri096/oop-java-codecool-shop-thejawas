package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/", "/index"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        String browseCategory = req.getParameter("category");

        String htmlFileName;
        int categoryId = (browseCategory == null) ? 1 : Integer.parseInt(browseCategory);
        switch (categoryId) {
            case 1:
                htmlFileName = "tablet";
                break;
            case 2:
                htmlFileName = "energy";
                break;
            default:
                htmlFileName = "index";
                break;
        }
        String htmlPage = "product/" + htmlFileName + ".html";

        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.find(categoryId));
        params.put("products", productDataStore.getBy(productCategoryDataStore.find(categoryId)));

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariables(params);
        context.setVariable("recipient", "World");
        context.setVariable("category", productCategoryDataStore.find(categoryId));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(categoryId)));
        engine.process(htmlPage, context, resp.getWriter());
    }

}
