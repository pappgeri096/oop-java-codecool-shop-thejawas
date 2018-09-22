package com.codecool.shop.controller;


import com.codecool.shop.config.Initializer;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
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
import java.util.Map;


@WebServlet(urlPatterns = {"/paypal"})
public class PaypalController extends HttpServlet {

    private static final Logger paypalLogger = LoggerFactory.getLogger(PaymentController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();

    private Map<String, Integer> productNameAndQuantityMap = cartDataManager.getProductNameAndQuantityMap();  // TODO: CARTDAO REFACTOR
    private Map<String, String> customerDataMap = customerDataManager.getCustomerDataMap();

//    private CartDao cartDaoSql = CartDaoSql.getInstance();
//    private CustomerDao customerDaoSql = CustomerDaoSql.getInstance();
//    private Map<String, String> userDataMap = customerDaoSql.getCustomerDataMap();
//    private Map<String, Integer> productNameAndQuantityMap = cartDaoSql.getProductNameAndQuantityMap();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (customerDataMap.size() == 0 || productNameAndQuantityMap.size() == 0) {
            resp.sendRedirect("/");
            paypalLogger.debug("Empty user data or product quantity");
            paypalLogger.debug(
                    "User data size: {}. Product quantity: {}",
                    customerDataMap.size(),
                    productNameAndQuantityMap.size());
        }
        paypalLogger.info("Customer is now redirected to paypal.com");
        //execute payment
        payment(resp);

    }


    private void payment(HttpServletResponse resp) throws IOException {
        ShippingAddress address = getAddress();

        List<Item> paypalItemLists = new ArrayList<>();
        getItems(paypalItemLists);

        ItemList itemList = getItemList(address, paypalItemLists);

        Amount amount = getAmount();

        List<Transaction> transactions = getTransactions(itemList, amount);

        PayerInfo info = getPayerInfo(address);

        Payer payer = getPayer(info);

        RedirectUrls redirectUrls = getRedirectUrls();

        Payment payment = getPayment(transactions, payer, redirectUrls);

        executePayment(resp, payment);
    }

    private void getItems(List<Item> payPalItems) {
        Cart currentCart = cartDataManager.getLastCart();

        for (Map.Entry<String, Integer> entry : productNameAndQuantityMap.entrySet()) {

            String name = entry.getKey();
            String quantity = Integer.toString(entry.getValue());
            String price = "1.1";

            for (CartItem cartItem : currentCart.getCartItemList()) {
                if (cartItem.getProduct().getName().equals(name)) {
                    price = String.valueOf(cartItem.getProduct().getDefaulPrice());
                    break;
                }

            }

            addCartItem(payPalItems, name, quantity, price);
        }
    }

    private ItemList getItemList(ShippingAddress address, List<Item> payPalItems) {
        ItemList list = new ItemList();
        list.setItems(payPalItems);
        list.setShippingAddress(address);
        return list;
    }



    private void addCartItem(List<Item> payPalItems, String name, String quantity, String price) {

        Item payPalItem = new Item();

        payPalItem.setName(name);
        payPalItem.setPrice(price);
        payPalItem.setCategory("PHYSICAL");
        payPalItem.setQuantity(quantity);
        payPalItem.setCurrency("USD");
        
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

    private Amount getAmount() {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(Double.toString(cartDataManager.getTotalPriceOfLastCart().doubleValue()));
        return amount;
    }

    private ShippingAddress getAddress() {
        ShippingAddress address = new ShippingAddress();
        address.setPhone(customerDataMap.get("telephoneNumber"));
        address.setCountryCode("HU");
        address.setCity(customerDataMap.get("cityBill"));
        address.setLine1(customerDataMap.get("addressBill"));
        address.setPostalCode(customerDataMap.get("zipCodeBill"));
        address.setState(customerDataMap.get("Pest"));
        return address;
    }

}
