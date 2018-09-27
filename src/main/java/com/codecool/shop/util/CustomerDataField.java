package com.codecool.shop.util;

public enum CustomerDataField {
    USER_ID("id"),
    USER_NAME("name"),
    PASSWORD_HASH("password_hash"),
    PASSWORD_CONFIRMATION("password_confirmation"),
    EMAIL_ADDRESS("email"),
    PHONE_NUMBER("phone_number"),
    COUNTRY_BILLING("billing_country"),
    CITY_BILLING("billing_city"),
    ZIP_CODE_BILLING("billing_zipcode"),
    ADDRESS_BILLING("billing_address"),
    SHIPPING_ADDRESS_SAME("sameAddress"),
    COUNTRY_SHIPPING("shipping_country"),
    CITY_SHIPPING("shipping_city"),
    ZIP_CODE_SHIPPING("shipping_zipcode"),
    ADDRESS_SHIPPING("shipping_address");

    private final String inputString;

    CustomerDataField(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }
}
