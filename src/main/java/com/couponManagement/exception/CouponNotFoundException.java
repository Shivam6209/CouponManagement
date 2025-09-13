package com.couponManagement.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(String couponId) {
        super("Coupon not found with ID: " + couponId);
    }

    public CouponNotFoundException(String couponId, Throwable cause) {
        super("Coupon not found with ID: " + couponId, cause);
    }
}
