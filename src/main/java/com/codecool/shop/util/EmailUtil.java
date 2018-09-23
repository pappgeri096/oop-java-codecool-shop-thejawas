package com.codecool.shop.util;


import com.codecool.shop.config.Initializer;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Cart;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.codecool.shop.util.implementation_factory.ImplementationFactory;

public class EmailUtil {

    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private static CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();
    private static CustomerDao customerDataManager = IMPLEMENTATION_FACTORY.getCustomerDataManagerInstance();


    private static void sendEmail(String email, String subject, String msg){

            // Sender's email ID needs to be mentioned
            String from = "thejawas@codecool.com";
            final String username = "thejawas2018";//change accordingly
            final String password = "CCShop18";//change accordingly

            // Assuming you are sending email through relay.jangosmtp.net
            String host = "smtp.gmail.com";

            Properties props = getProperties();
            Session session = getSession(username, password, props);


            send(email, subject, msg, from, session);
    }

    private static void send(String email, String subject, String msg, String from, Session session) {
        try {
                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email));

                // Set Subject: header field
                message.setSubject(subject);

                // Now set the actual message
                message.setText(msg);

                // Send message
                Transport.send(message);

                System.out.println("Sent message successfully....");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
    }

    private static Session getSession(String username, String password, Properties props) {
        // Get the Session object.
        return Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    public static void sendVerificationEmail() {

        Cart currentOrder = cartDataManager.getLastCart();

        Map<String, String> customerDataMap = customerDataManager.getCustomerDataMap();
//        Map<String, String> userDataMap = ((CartDaoMem) cartDaoMem).getCustomerDataMap();


        List<CartItem> cartItemList = currentOrder.getCartItemList();

        String subject = "Order#"+ currentOrder.getId();
        String email = customerDataMap.get("emailAddress");

        StringBuilder message = new StringBuilder();

        message.append("Name: ").append(customerDataMap.get("fullName")).append("\n");
        message.append("Email: ").append(email).append("\n");
        message.append("Items:");

        for(CartItem item : cartItemList){
            message.append(item.getProduct().getName()).append(" ");
            message.append(item.getSubTotalPrice()).append("\n");
        }


        sendEmail(email, subject, message.toString());
    }

}