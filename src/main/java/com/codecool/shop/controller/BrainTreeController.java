package com.codecool.shop.controller;

import com.braintreegateway.*;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/braintree"})
public class BrainTreeController  extends HttpServlet {

    private CartDao cartHandler = CartDaoMem.getInstance();

//    private CartDao cartHandler = CartDaoSql.getInstance();

    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "r63vmkrc39s63j9m",
            "zt4qsvng8hnygtg8",
            "8fbed28cb5f457cbe3ff1359091f18af"
    );


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        String nonce = req.getParameter("payment_method_nonce");
        TransactionRequest request = new TransactionRequest()
                .amount(cartHandler.getTotalPrice())
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction settledTransaction = result.getTarget(); // TODO: DEAD CODE: ARE WE GONNA NEED THIS?
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
