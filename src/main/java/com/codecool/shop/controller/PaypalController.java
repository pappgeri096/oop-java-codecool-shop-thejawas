package com.codecool.shop.controller;


import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = {"/paypal"})
public class PaypalController extends HttpServlet {

    private String clientID = "AWTqmvOfxu2VnNifNQblRmD8ty6zvuam7Hh_k36MHk8sbYuZdEtR3gneLyuK_3A7E_AzZm0AWr-rNVA3";
    private String SecretID = "ECYgXqdlLBxQsCHhdwMt4yz1LU5O5n6chmJe3EHrhGftsUOiN5PbmergN_0_lqQcFl-JzzC1ep68JG5I";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ShippingAddress address = new ShippingAddress();
        //address.setRecipientName("Janos Istvan");
        address.setPhone("32523523");
        address.setCountryCode("HU");
        address.setCity("Budapest");
        address.setLine1("IDK");
        address.setPostalCode("2600");
        address.setState("Pest");

        Item item = new Item();
        item.setName("WC PAPIR");
        item.setPrice("5000");
        item.setCategory("PHYSICAL");
        item.setQuantity("2");
        item.setCurrency("HUF");

        List items2 = new ArrayList();
        items2.add(item);
        //items2.add(item2);

        ItemList list = new ItemList();
        list.setItems(items2);
        list.setShippingAddress(address);


        Details details = new Details();
        details.setShipping("5");
        details.setSubtotal("10000");

        Amount amount = new Amount();
        amount.setCurrency("HUF");
        amount.setTotal("10005");
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(list);
        transaction.setDescription("cunci");
        transaction.setInvoiceNumber("23429999");
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);


        PayerInfo info = new PayerInfo();
        info.setLastName("Bill");
        info.setFirstName("Gates");
        info.setBillingAddress(address);

        Payer payer = new Payer();
        payer.setPayerInfo(info);
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/done");


        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);


        try {
            APIContext apiContext = new APIContext(clientID, SecretID, "sandbox");
            Payment createdPayment = payment.create(apiContext);

            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    // REDIRECT USER TO link.getHref()
                    resp.sendRedirect(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }


    }

}
