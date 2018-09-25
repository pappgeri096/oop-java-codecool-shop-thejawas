package com.codecool.shop.controller;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(urlPatterns = {"/review"})
public class ReviewController extends HttpServlet {

    private static final Logger reviewLogger = LoggerFactory.getLogger(PaymentController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private final CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("cartDataManager", cartDataManager);
        context.setVariable("numberOfProductsInLastCart", cartDataManager.getLastCart().getCartItemList().size());
        context.setVariable("totalPrice", cartDataManager.getTotalPriceOfLastCart());
        engine.process("product/review.html", context, resp.getWriter());
        reviewLogger.info("Get request received for SHOPPING CART REVIEW page");
    }
}
