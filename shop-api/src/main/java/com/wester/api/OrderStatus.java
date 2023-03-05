package com.wester.api;

public enum OrderStatus {
    PENDING(),
    DELIVERY();

    public String getName() {
        return  name().toLowerCase();
    }
    //    public static final String DELETED = "deleted";
}
