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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;


@WebServlet(urlPatterns = {"/paypal"})
public class PaypalController extends HttpServlet {

    private String clientID;
    private String SecretID;
    private OrderDao orderDataStore;
    private Order order;
    private static final Logger paypalLogger = LoggerFactory.getLogger(PaymentController.class);


    public PaypalController() {
        this.clientID = "AWTqmvOfxu2VnNifNQblRmD8ty6zvuam7Hh_k36MHk8sbYuZdEtR3gneLyuK_3A7E_AzZm0AWr-rNVA3";
        SecretID = "ECYgXqdlLBxQsCHhdwMt4yz1LU5O5n6chmJe3EHrhGftsUOiN5PbmergN_0_lqQcFl-JzzC1ep68JG5I";
        this.orderDataStore = OrderDaoMem.getInstance();
        this.order = orderDataStore.getCurrent();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (order.getUserDataMap().size() == 0 || order.getProductNameAndQuantityMap().size() == 0) {
            resp.sendRedirect("/");
            paypalLogger.debug("Empty user data or product quantity");
            paypalLogger.debug(
                    "User data size: {}. Product quantity: {}",
                    order.getUserDataMap().size(),
                    order.getProductNameAndQuantityMap().size());
        }

        //execute payment
        payment(resp);
        paypalLogger.debug("Customer is now redirected to paypal.com");

    }

    private void payment(HttpServletResponse resp) throws IOException {
        ShippingAddress address = getAddress();

        List items = new ArrayList();
        getItems(items);

        ItemList list = getItemList(address, items);

        Amount amount = getAmount();

        List<Transaction> transactions = getTransactions(list, amount);

        PayerInfo info = getPayerInfo(address);

        Payer payer = getPayer(info);

        RedirectUrls redirectUrls = getRedirectUrls();

        Payment payment = getPayment(transactions, payer, redirectUrls);

        executePayment(resp, payment);
    }

    private void executePayment(HttpServletResponse resp, Payment payment) throws IOException {
        try {
            APIContext apiContext = new APIContext(clientID, SecretID, "sandbox");
            Payment createdPayment = payment.create(apiContext);

            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    resp.sendRedirect(link.getHref());
                    paypalLogger.debug("Payment approved by PayPal");
                }
            }
        } catch (PayPalRESTException e) {
            paypalLogger.error("PayPal REST exception: {}", e.getDetails().getMessage());
            resp.sendRedirect("/cancel");
        }
    }

    private Payment getPayment(List<Transaction> transactions, Payer payer, RedirectUrls redirectUrls) {
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/done");
        return redirectUrls;
    }

    private Payer getPayer(PayerInfo info) {
        Payer payer = new Payer();
        payer.setPayerInfo(info);
        payer.setPaymentMethod("paypal");
        return payer;
    }

    private PayerInfo getPayerInfo(ShippingAddress address) {
        PayerInfo info = new PayerInfo();
        info.setLastName("Bill");
        info.setFirstName("Gates");
        info.setBillingAddress(address);
        return info;
    }

    private List<Transaction> getTransactions(ItemList list, Amount amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(list);
        transaction.setDescription("Payment page");
        //transaction.setInvoiceNumber(Integer.toString(ThreadLocalRandom.current().nextInt(1000000000, 999999999 + 1)));
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        return transactions;
    }

    private Amount getAmount() {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(Float.toString(order.getPriceOfAllProducts()));
        return amount;
    }

    private ItemList getItemList(ShippingAddress address, List items) {
        ItemList list = new ItemList();
        list.setItems(items);
        list.setShippingAddress(address);
        return list;
    }

    private void getItems(List items) {
        for (Map.Entry<String, Integer> entry : order.getProductNameAndQuantityMap().entrySet()) {


            String name = entry.getKey();
            String quantity = Integer.toString(entry.getValue());
            String price = "1.1";

            for (LineItem item : order.getLineItemList()) {
                if (item.getProduct().getName().equals(name)) {
                    price = String.valueOf(item.getProduct().getDefaulPrice());
                    break;
                }

            }

            addItem(items, name, quantity, price);
        }
    }

    private void addItem(List items, String name, String quantity, String price) {
        Item item = new Item();
        item.setName(name);

        Double totalPrice = Integer.parseInt(quantity) * Double.parseDouble(price);
        BigDecimal totalPriceRounded = new BigDecimal(totalPrice);
        totalPriceRounded = totalPriceRounded.setScale(2, RoundingMode.HALF_UP);
        item.setPrice(totalPriceRounded.toString());
        item.setCategory("PHYSICAL");
        item.setQuantity(quantity);
        item.setCurrency("USD");
        items.add(item);
    }

    private ShippingAddress getAddress() {
        ShippingAddress address = new ShippingAddress();
        //address.setRecipientName("Janos Istvan");
        address.setPhone(order.getUserDataMap().get("telephoneNumber"));
        address.setCountryCode("HU");
        address.setCity(order.getUserDataMap().get("cityBill"));
        address.setLine1(order.getUserDataMap().get("addressBill"));
        address.setPostalCode(order.getUserDataMap().get("zipCodeBill"));
        address.setState(order.getUserDataMap().get("Pest"));
        return address;
    }

}
