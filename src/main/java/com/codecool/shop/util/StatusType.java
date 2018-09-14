package com.codecool.shop.util;

public enum StatusType {
    EMPTY("EMPTY"),
    UNFINISHED("UNFINISHED"),
    PAID("PAID"),
    UNSHIPPED("UNSHIPPED"),
    SHIPPED("SHIPPED");

    private final String statusString;

    StatusType(String statusString) {
        this.statusString = statusString;
    }

    public String getStatusString() {
        return statusString;
    }
}
