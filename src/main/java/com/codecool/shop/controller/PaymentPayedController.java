package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.dao.implementation.postgresql.OrderDaoSql;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(urlPatterns = {"/success"})
public class PaymentPayedController extends HttpServlet {

    private static final Logger paymentPayedLogger = LoggerFactory.getLogger(PaymentPayedController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        List<LineItem> lineItemList = orderDataStore.getCurrent().getLineItemList();
//        lineItemList.clear();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("payment/payed.html", context, resp.getWriter());
        OrderDaoSql.getSingletonInstance().add(OrderDaoMem.getInstance().getCurrent());
        OrderDaoMem.getInstance().add(new Order());
        paymentPayedLogger.info("Payment approved by PayPal");
    }

}
