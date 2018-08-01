package com.codecool.shop.controller;


import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.Memory.OrderDaoMem;
import com.codecool.shop.model.LineItem;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

        private static void sendEmail(String email, String subject, String msg){

            // Sender's email ID needs to be mentioned
            String from = "thejawas@codecool.com";
            final String username = "thejawas2018";//change accordingly
            final String password = "CCShop18";//change accordingly

            // Assuming you are sending email through relay.jangosmtp.net
            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

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

    static void sendVerificationEmail() {

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        List<LineItem> lineItemList = orderDataStore.getCurrent().getLineItemList();

        String subject = "Order#"+orderDataStore.getCurrent().getId();
        String email = orderDataStore.getCurrent().getUserDataMap().get("emailAddress");

        StringBuilder message = new StringBuilder();

        message.append("Name: ").append(orderDataStore.getCurrent().getUserDataMap().get("fullName")).append("\n");
        message.append("Email: ").append(email).append("\n");
        message.append("Items:");

        for(LineItem item : lineItemList){
            message.append(item.getProduct().getName()).append(" ");
            message.append(item.getSubTotalPrice()).append("\n");
        }


        sendEmail(email, subject, message.toString());
    }

}