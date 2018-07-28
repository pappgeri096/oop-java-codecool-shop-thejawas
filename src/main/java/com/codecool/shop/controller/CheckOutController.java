package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.JSON.OrderDaoJson;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    private static final Logger checkoutLogger = LoggerFactory.getLogger(CheckOutController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("product/checkout.html", context, resp.getWriter());
        checkoutLogger.trace("Get request received for CHECKOUT page");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getCurrent();
        List<String> userData = new ArrayList<>();
        List<String> formNames = Arrays.asList("name", "email", "phonenumber", "countryBill",
                "cityBill", "zipcodeBill",
                "addressBill", "sameAddress", "countryShip", "cityShip",
                "zipcodeShip", "addressShip");
        for (String formName : formNames) {
            if (formName.equals("sameAddress") && req.getParameter(formName) != null && req.getParameter(formName).equals("true")) {
                for (int i = 3; i < 7; i++) {
                    userData.add(userData.get(i));
                }
                break;
            } else if (!formName.equals("sameAddress")) {
                userData.add(req.getParameter(formName));
            }
        }
        order.setUserData(userData);

        OrderDao writeOrderDataToFile = new OrderDaoJson();
        writeOrderDataToFile.add(order);

//        OrderDao serializeOrder = new OrderDaoJson();
//        String serializedOrder = ((OrderDaoJson) serializeOrder).orderToJsonString(order);
//        checkoutLogger.warn(serializedOrder);

        String uuidString = ((OrderDaoJson) writeOrderDataToFile).getUuidString();
        checkoutLogger.trace("User data is saved in json file. Order ID: {}", uuidString);

        resp.sendRedirect("/payment");
    }
}

