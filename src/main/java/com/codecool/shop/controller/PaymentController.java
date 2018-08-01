package com.codecool.shop.controller;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.codecool.shop.config.TemplateEngineUtil;
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


@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "r63vmkrc39s63j9m",
            "zt4qsvng8hnygtg8",
            "8fbed28cb5f457cbe3ff1359091f18af"
    );

    private static final Logger paymentLogger = LoggerFactory.getLogger(PaymentController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String token = gateway.clientToken().generate();
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("token", token);
        engine.process("payment/index.html", context, resp.getWriter());
        paymentLogger.info("Get request received for PAYMENT page");
        Email.sendEmail();
    }

}