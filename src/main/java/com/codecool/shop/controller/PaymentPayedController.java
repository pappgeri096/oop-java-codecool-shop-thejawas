package com.codecool.shop.controller;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.util.CartStatusType;
import com.codecool.shop.util.EmailUtil;
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


@WebServlet(urlPatterns = {"/success"})
public class PaymentPayedController extends HttpServlet {

    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();

    private static final Logger paymentPayedLogger = LoggerFactory.getLogger(PaymentPayedController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EmailUtil.sendVerificationEmail();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("payment/payed.html", context, resp.getWriter());


        cartDataManager.updateLastCartStatus(CartStatusType.UNSHIPPED);

        cartDataManager.add(new Cart(cartDataManager.generateIdForNewCart(), customerDataManager.getGuestId(), CartStatusType.EMPTY));

        paymentPayedLogger.info("Payment approved by online payment service provider");

    }



}
