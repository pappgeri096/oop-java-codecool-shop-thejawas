package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.postgresql.CartDaoSql;
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


@WebServlet(urlPatterns = {"/success"})
public class PaymentPayedController extends HttpServlet {

    private static final Logger paymentPayedLogger = LoggerFactory.getLogger(PaymentPayedController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CartDao orderDataMem = CartDaoMem.getInstance();
        CartDao cartDaoSql = CartDaoSql.getInstance();

        EmailUtil.sendVerificationEmail();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("payment/payed.html", context, resp.getWriter());
        cartDaoSql.add(orderDataMem.getCurrent());
        orderDataMem.add(new Cart());
        paymentPayedLogger.info("Payment approved by online payment service provider");

    }



}
