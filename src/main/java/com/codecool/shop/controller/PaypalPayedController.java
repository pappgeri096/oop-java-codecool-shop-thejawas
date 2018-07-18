package com.codecool.shop.controller;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/done"})
public class PaypalPayedController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String clientId = "AWTqmvOfxu2VnNifNQblRmD8ty6zvuam7Hh_k36MHk8sbYuZdEtR3gneLyuK_3A7E_AzZm0AWr-rNVA3";
        String clientSecret = "ECYgXqdlLBxQsCHhdwMt4yz1LU5O5n6chmJe3EHrhGftsUOiN5PbmergN_0_lqQcFl-JzzC1ep68JG5I";

        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("PayerID"));
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            System.out.println(createdPayment);

        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }

    }

}
