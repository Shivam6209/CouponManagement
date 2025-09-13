package com.couponManagement.service;

import com.couponManagement.dto.*;
import java.util.List;

public interface CouponService {

    CouponDto createCoupon(CreateCouponRequest request);
    CouponDto getCouponById(Long couponId);
    List<CouponDto> getAllCoupons();
    List<CouponDto> getActiveCoupons();
    CouponDto updateCoupon(Long couponId, CreateCouponRequest request);
    void deleteCoupon(Long couponId);

    ApplicableCouponsResponse getApplicableCoupons(CartDto cart);
    ApplyCouponResponse applyCoupon(Long couponId, CartDto cart);
}
