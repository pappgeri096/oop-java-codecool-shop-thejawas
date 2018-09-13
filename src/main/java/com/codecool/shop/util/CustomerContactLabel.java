package com.codecool.shop.util;

public enum CustomerContactLabel {
    USER_NAME("name"),
    EMAIL_ADDRESS("email"),
    PHONE_NUMBER("phonenumber"),
    COUNTRY_BILLING("countryBill"),
    CITY_BILLING("cityBill"),
    ZIP_CODE_BILLING("zipcodeBill"),
    ADDRESS_BILLING("addressBill"),
    SHIPPING_ADDRESS_SAME("sameAddress"),
    COUNTRY_SHIPPING("countryShip"),
    CITY_SHIPPING("cityShip"),
    ZIP_CODE_SHIPPING("zipcodeShip"),
    ADDRESS_SHIPPING("addressShip");

    private final String inputString;

    CustomerContactLabel(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }
}
