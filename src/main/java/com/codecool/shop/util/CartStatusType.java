package com.codecool.shop.util;

public enum CartStatusType {
    EMPTY("EMPTY"),
    UNFINISHED("UNFINISHED"),
    SAVED_BY_USER("SAVED_BY_USER"),
    UNSHIPPED("UNSHIPPED"),
    SHIPPED("SHIPPED");

    private final String statusString;

    CartStatusType(String statusString) {
        this.statusString = statusString;
    }

    public String getStatusString() {
        return statusString;
    }
}
