package com.codecool.shop.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codecool.shop.util.PasswordUtil.getSalt;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    private PasswordUtil passwordUtil = new PasswordUtil();

    String testPassword = "PassWord123";
    String salt = passwordUtil.getSalt(testPassword.length());

    @Test
    void testSaltCorrectLength(){
        System.out.println(passwordUtil.getSalt(testPassword.length()));
        assertEquals(testPassword.length(), passwordUtil.getSalt(testPassword.length()).length());
    }

    @Test
    void testGeneratingHashedPassword(){
        System.out.println(passwordUtil.generateSecurePassword(testPassword, passwordUtil.getSalt(testPassword.length())));
        assertNotEquals(testPassword, passwordUtil.generateSecurePassword(testPassword, passwordUtil.getSalt(testPassword.length())));
    }

    @Test
    void testMatchingPassword(){
        assertTrue(passwordUtil.verifyUserPassword(testPassword, passwordUtil.generateSecurePassword(testPassword, salt), salt));
    }

    @Test
    void testWrongPassword(){
        assertFalse(passwordUtil.verifyUserPassword("abc", passwordUtil.generateSecurePassword(testPassword, salt), salt));
    }


}