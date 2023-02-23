package com.wester.shop.entity;

public enum GoodsStatus {
    OK(),
    DELETED();

    public String getName() {
        return  name().toLowerCase();
    }
    //    public static final String DELETED = "deleted";
}
