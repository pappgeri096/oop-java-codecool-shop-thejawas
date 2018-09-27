package com.codecool.shop.util;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static final SessionUtil INSTANCE = new SessionUtil();
    private HttpSession session;

    private SessionUtil() {}

    public static SessionUtil getInstance() {
        return INSTANCE;
    }

    public void addAttributeToSession(String key, String value) {
        session.setAttribute(key, value);
    }

    public void addAttributeToSession(String key, int value) {
        session.setAttribute(key, value);
    }

    public String getSessionValue(String key) {
        return session.getAttribute(key).toString();
    }
}
