package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.CustomerDaoMem;
import com.codecool.shop.model.Customer;
import com.codecool.shop.util.CustomerContactLabel;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    private CustomerDao customerDao = CustomerDaoMem.getInstance();

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
        List<String> customerData = new ArrayList<>();

        String SAME_ADDRESS_INPUT = req.getParameter("sameAddress");
        for (CustomerContactLabel labelEnum : CustomerContactLabel.values()) {
            String enumInString = labelEnum.getInputString();
            if (enumInString.equals("sameAddress") && SAME_ADDRESS_INPUT != null && SAME_ADDRESS_INPUT.equals("true")) {
                for (int i = 3; i < 7; i++) {
                    customerData.add(customerData.get(i));
                }
                break;
            } else if (!enumInString.equals("sameAddress")) {
                customerData.add(req.getParameter(enumInString));
            }
        }

        customerDao.add(new Customer(customerData));
        customerDao.createUserDataMap();

        resp.sendRedirect("/payment");
    }
}

