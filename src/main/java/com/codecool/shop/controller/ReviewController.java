package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
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

    private CartDao cartDataManager = CartDaoMem.getInstance();

//    private CartDao cartDaoSql = CartDaoSql.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart currentOrder = cartDataManager.getCurrent();


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("orderMem", currentOrder);
        context.setVariable("totalPrice", cartDataManager.getTotalPrice());
        engine.process("product/review.html", context, resp.getWriter());
        reviewLogger.info("Get request received for SHOPPING CART REVIEW page");
    }
}
