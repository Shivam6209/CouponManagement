package com.couponManagement.exception;

public class CouponAlreadyExistsException extends RuntimeException {

    public CouponAlreadyExistsException(String couponCode) {
        super("Coupon already exists with code: " + couponCode);
    }

    public CouponAlreadyExistsException(String couponCode, Throwable cause) {
        super("Coupon already exists with code: " + couponCode, cause);
    }
}
