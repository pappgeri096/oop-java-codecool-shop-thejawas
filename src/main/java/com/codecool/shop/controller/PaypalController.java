package com.codecool.shop.controller;


import com.codecool.shop.config.Initializer;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Customer;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@WebServlet(urlPatterns = {"/paypal"})
public class PaypalController extends HttpServlet {

    private static final Logger paypalLogger = LoggerFactory.getLogger(PaymentController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int numberOfProductsInCart = cartDataManager.getLastCart().getCartItemList().size();

        boolean anyCustomerDataMissing = customerDataManager.checkIfAnyCustomerDataMissing();
        if (anyCustomerDataMissing || numberOfProductsInCart == 0) {
            resp.sendRedirect("/");
            paypalLogger.debug("Not enough user data provided or no product was selected fot purchase");
            paypalLogger.debug(
                    "All user data are provided: {}. Product quantity: {}",
                    anyCustomerDataMissing,
                    numberOfProductsInCart
            );
        } else {
            paypalLogger.info("Customer is now redirected to paypal.com");
            //execute payment
            payment(resp);

        }
    }


    private void payment(HttpServletResponse resp) throws IOException {
        ShippingAddress address = getAddress();

        List<Item> paypalItemLists = new ArrayList<>();
        getItems(paypalItemLists);

        ItemList itemList = getItemList(address, paypalItemLists);

        String currency = String.valueOf(cartDataManager.getCurrencyFromLastCartBy(cartDataManager.getLastCart().getCartItemList().get(0).getProduct().getId()));

        Amount amount = getAmount(currency);

        List<Transaction> transactions = getTransactions(itemList, amount);

        PayerInfo info = getPayerInfo(address);

        Payer payer = getPayer(info);

        RedirectUrls redirectUrls = getRedirectUrls();

        Payment payment = getPayment(transactions, payer, redirectUrls);

        executePayment(resp, payment);
    }

    private void getItems(List<Item> payPalItems) {
        List<CartItem> itemsIncCurrentCart = cartDataManager.getLastCart().getCartItemList();

        for (CartItem cartItem : itemsIncCurrentCart) {

            String name = cartItem.getProduct().getName();
            String quantity = Integer.toString(cartItem.getQuantity());
            String price = String.valueOf(cartItem.getProduct().getDefaulPrice());
            String currency = String.valueOf(cartDataManager.getCurrencyFromLastCartBy(cartItem.getProduct().getId()));

            addCartItem(payPalItems, name, quantity, price, currency);
        }
    }

    private ItemList getItemList(ShippingAddress address, List<Item> payPalItems) {
        ItemList list = new ItemList();
        list.setItems(payPalItems);
        list.setShippingAddress(address);
        return list;
    }



    private void addCartItem(List<Item> payPalItems, String name, String quantity, String price, String currency) {

        Item payPalItem = new Item();

        payPalItem.setName(name);
        payPalItem.setPrice(price);
        payPalItem.setCategory("PHYSICAL");
        payPalItem.setQuantity(quantity);
        payPalItem.setCurrency(currency);
        
        payPalItems.add(payPalItem);
    }


    private void executePayment(HttpServletResponse resp, Payment payment) throws IOException {
        try {
            String clientID = "AWTqmvOfxu2VnNifNQblRmD8ty6zvuam7Hh_k36MHk8sbYuZdEtR3gneLyuK_3A7E_AzZm0AWr-rNVA3";
            String secretID = "ECYgXqdlLBxQsCHhdwMt4yz1LU5O5n6chmJe3EHrhGftsUOiN5PbmergN_0_lqQcFl-JzzC1ep68JG5I";

            APIContext apiContext = new APIContext(clientID, secretID, "sandbox");
            Payment createdPayment = payment.create(apiContext);

            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    resp.sendRedirect(link.getHref());
                    paypalLogger.info("Payment approved by PayPal");
                }
            }
        } catch (PayPalRESTException e) {
            paypalLogger.error("Payment is not approved by ", e);
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
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        return transactions;
    }

    private Amount getAmount(String currency) {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(Double.toString(cartDataManager.getTotalPriceOfLastCart().doubleValue()));
        return amount;
    }

    private ShippingAddress getAddress() {
        ShippingAddress address = new ShippingAddress();
        Customer currentCustomer = customerDataManager.getCurrent();
        address.setPhone(String.valueOf(currentCustomer.getPhoneNumber()));
        address.setCountryCode("HU");
        address.setCity(currentCustomer.getBillingCity());
        address.setLine1(currentCustomer.getBillingAddress());
        address.setPostalCode(currentCustomer.getBillingZipCode());
        address.setState("Pest"); // TODO: GET STATE AUTOMATICALLY BASED ON CITY NAME
        return address;
    }

}
