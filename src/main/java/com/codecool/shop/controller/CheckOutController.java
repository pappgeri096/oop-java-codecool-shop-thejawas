package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.JSON.CartDaoJson;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
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
        checkoutLogger.info("Get request received for CHECKOUT page");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CartDao orderDataStore = CartDaoMem.getInstance();
        Cart orderMem = orderDataStore.getCurrent();
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
        ((CartDaoMem) orderDataStore).createUserDataMap(userData);

        CartDaoJson writeOrderDataToFile = new CartDaoJson();
        writeOrderDataToFile.add(orderMem);

//        CartDao serializeOrder = new CartDaoJson();
//        String serializedOrder = ((CartDaoJson) serializeOrder).orderToJsonString(orderMem);
//        checkoutLogger.warn(serializedOrder);

        String uuidString = writeOrderDataToFile.getUuidString();
        checkoutLogger.info("User data is saved in json file. OrderFromMemory ID: {}", uuidString);

        resp.sendRedirect("/payment");
    }
}

