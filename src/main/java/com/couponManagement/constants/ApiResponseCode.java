package com.couponManagement.constants;

public enum ApiResponseCode {
    SUCCESS(0),
    FAILURE(-1);

    private final int code;

    ApiResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
