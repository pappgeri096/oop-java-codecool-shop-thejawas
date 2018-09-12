package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.LineItem;
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


@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    private static final Logger cartLogger = LoggerFactory.getLogger(PaymentController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CartDao orderDataStore = CartDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("orderMem", orderDataStore.getCurrent());
        engine.process("product/cart.html", context, resp.getWriter());

        cartLogger.info("Shopping cart editor page displayed");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CartDao cartDaoMem = CartDaoMem.getInstance();
        Cart currentOrder = cartDaoMem.getCurrent();
        List<LineItem> lineItemList = currentOrder.getLineItemList();
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (LineItem lineItem : lineItemList) {
                int newQuantity = Integer.parseInt(req.getParameter(String.valueOf(lineItem.id)));
                if (newQuantity < 1) {
                    lineItemList.remove(lineItem);
                    cartLogger.info("{} is removed from shopping cart.", lineItem.getProduct().getName());
                    repeat = true;
                    break;

                } else if (newQuantity != lineItem.getQuantity()){
                    lineItem.setQuantity(newQuantity);
                    cartLogger.info("New quantity for {} is set to {}", lineItem.getProduct().getName(), newQuantity);
                }
            }
        }
        ((CartDaoMem) cartDaoMem).createProductNameAndQuantityMaps();
        if (currentOrder.getLineItemList().size()>0) {
            resp.sendRedirect("/review");
        }else {
            resp.sendRedirect("/");
            cartLogger.info("All products were removed from shopping cart.");
        }

    }
}

