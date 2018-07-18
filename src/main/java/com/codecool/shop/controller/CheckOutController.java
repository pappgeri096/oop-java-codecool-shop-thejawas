package com.codecool.shop.controller;

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
        List<String> userData = new ArrayList<>();
        List<String> formNames = Arrays.asList("name","email","phonenumber","countryBill",
                "cityBill","zipcodeBill",
                "addressBill","sameAddress","countryShip","cityShip",
                "zipcodeShip","addressShip");
        for (String formName:formNames) {
            if (formName.equals( "sameAddress") && req.getParameter(formName).equals("true")){
                for (int i = 3; i < 7; i++) {
                    userData.add(userData.get(i));
                }
                break;
            }else if(formName.equals( "sameAddress")){
                continue;
            }else {
                userData.add(req.getParameter(formName));
            }
        }
        System.out.println(userData);
        resp.sendRedirect("/payment");

    }
}

