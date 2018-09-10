package com.codecool.shop.controller;

import com.braintreegateway.*;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/braintree"})
public class BrainTreeController  extends HttpServlet {

    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "r63vmkrc39s63j9m",
            "zt4qsvng8hnygtg8",
            "8fbed28cb5f457cbe3ff1359091f18af"
    );
    private Order order;

    public BrainTreeController() {
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        this.order = orderDataStore.getCurrent();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String nonce = req.getParameter("payment_method_nonce");;
        TransactionRequest request = new TransactionRequest()
                .amount(order.getTotalPrice())
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction settledTransaction = result.getTarget();
            resp.sendRedirect("/success");
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                System.out.println(error.getCode());
                System.out.println(error.getMessage());
                System.out.println();
            }
            resp.sendRedirect("/cancel");

        }
    }
}
