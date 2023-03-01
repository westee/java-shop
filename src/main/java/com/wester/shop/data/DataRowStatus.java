package com.wester.shop.data;

public enum DataRowStatus {
    OK(),
    DELETED(),
    RECEIVED(),
    PENDING(),
    DELIVERED(),
    PAID();

    public static DataRowStatus fromStatus(String status) {
        try {
            if (status == null) {
                return null;
            }
            return DataRowStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getName() {
        return name().toLowerCase();
    }
}
