package com.codecool.shop.controller;


import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.LineItem;
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
import java.util.concurrent.ThreadLocalRandom;


@WebServlet(urlPatterns = {"/paypal"})
public class PaypalController extends HttpServlet {

    private String clientID = "AWTqmvOfxu2VnNifNQblRmD8ty6zvuam7Hh_k36MHk8sbYuZdEtR3gneLyuK_3A7E_AzZm0AWr-rNVA3";
    private String SecretID = "ECYgXqdlLBxQsCHhdwMt4yz1LU5O5n6chmJe3EHrhGftsUOiN5PbmergN_0_lqQcFl-JzzC1ep68JG5I";
    private OrderDao orderDataStore = OrderDaoMem.getInstance();;
    private Order order = orderDataStore.getCurrent();;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(order.getUserDataMap().size()==0 || order.getProductNameAndQuantityMap().size()==0)
            resp.sendRedirect("/");

        ShippingAddress address = new ShippingAddress();
        //address.setRecipientName("Janos Istvan");
        address.setPhone(order.getUserDataMap().get("telephoneNumber"));
        address.setCountryCode("HU");
        address.setCity(order.getUserDataMap().get("cityBill"));
        address.setLine1(order.getUserDataMap().get("addressBill"));
        address.setPostalCode(order.getUserDataMap().get("zipCodeBill"));
        address.setState(order.getUserDataMap().get("Pest"));

        List items = new ArrayList();

        for (Map.Entry<String, Integer> entry : order.getProductNameAndQuantityMap().entrySet()) {


            String name = entry.getKey();
            String quantity = Integer.toString(entry.getValue());
            String price = "1.1";

            for(LineItem item  : order.getLineItemList()){
                if(item.getProduct().getName().equals(name)){
                    price = String.valueOf(item.getProduct().getDefaulPrice());
                    break;
                }

            }

            Item item = new Item();
            item.setName(name);
            item.setPrice(Double.toString(Integer.parseInt(quantity)*Double.parseDouble(price)));
            item.setCategory("PHYSICAL");
            item.setQuantity(quantity);
            item.setCurrency("USD");
            items.add(item);
        }


        ItemList list = new ItemList();
        list.setItems(items);
        list.setShippingAddress(address);


        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(Float.toString(order.getPriceOfAllProducts()));


        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(list);
        transaction.setDescription("Payment page");
        //transaction.setInvoiceNumber(Integer.toString(ThreadLocalRandom.current().nextInt(1000000000, 999999999 + 1)));
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
