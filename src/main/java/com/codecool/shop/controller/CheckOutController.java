package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {"/checkoutcontroller"})
public class CheckOutController extends HttpServlet {

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
        resp.sendRedirect("/payment");

    }
}

