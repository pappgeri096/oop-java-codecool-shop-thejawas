package com.codecool.shop.controller;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;
import com.codecool.shop.util.CustomerDataField;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();

//    private CustomerDao customerDaoSql = CustomerDaoSql.getInstance();

    private static final Logger checkoutLogger = LoggerFactory.getLogger(CheckOutController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("product/checkout.html", context, resp.getWriter());
        checkoutLogger.info("Get request received for CHECKOUT page");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        customerDataManager.add(createNewCustomerFromUserInput(req));

        resp.sendRedirect("/payment");
    }

    private Customer createNewCustomerFromUserInput(HttpServletRequest req) {
        String SHIPPING_ADDRESS_SAME_AS_BILLING = req.getParameter(CustomerDataField.SHIPPING_ADDRESS_SAME.getInputString());

        int id = customerDataManager.generateIdForNewCustomer();
        String name = req.getParameter(CustomerDataField.USER_NAME.getInputString());
        String email = req.getParameter(CustomerDataField.EMAIL_ADDRESS.getInputString());
        int phoneNumber = Integer.parseInt(req.getParameter(CustomerDataField.PHONE_NUMBER.getInputString()));
        String billingCountry = req.getParameter(CustomerDataField.COUNTRY_BILLING.getInputString());
        String billingCity = req.getParameter(CustomerDataField.CITY_BILLING.getInputString());
        String billingZipCode = req.getParameter(CustomerDataField.ZIP_CODE_BILLING.getInputString());
        String billingAddress = req.getParameter(CustomerDataField.ADDRESS_BILLING.getInputString());

        String shippingCountry;
        String shippingCity;
        String shippingZipCode;
        String shippingAddress;

        if (SHIPPING_ADDRESS_SAME_AS_BILLING != null && SHIPPING_ADDRESS_SAME_AS_BILLING.equals("true")) {
            shippingCountry = billingCountry;
            shippingCity = billingCity;
            shippingZipCode = billingZipCode;
            shippingAddress = billingAddress;

        } else {
            shippingCountry = req.getParameter(CustomerDataField.COUNTRY_SHIPPING.getInputString());
            shippingCity = req.getParameter(CustomerDataField.CITY_SHIPPING.getInputString());
            shippingZipCode = req.getParameter(CustomerDataField.ZIP_CODE_SHIPPING.getInputString());
            shippingAddress = req.getParameter(CustomerDataField.ADDRESS_SHIPPING.getInputString());

        }

        return new Customer(
                id, name, email, phoneNumber, billingCountry, billingCity, billingZipCode, billingAddress,
                shippingCountry, shippingCity, shippingZipCode, shippingAddress
        );
    }
}

