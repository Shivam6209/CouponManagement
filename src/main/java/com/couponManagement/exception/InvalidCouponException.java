package com.couponManagement.exception;

public class InvalidCouponException extends RuntimeException {

    public InvalidCouponException(String message) {
        super(message);
    }

    public InvalidCouponException(String message, Throwable cause) {
        super(message, cause);
    }
}
