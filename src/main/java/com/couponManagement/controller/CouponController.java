package com.couponManagement.controller;

import com.couponManagement.VO.ResultVO;
import com.couponManagement.constants.ApiResponseCode;
import com.couponManagement.dto.*;
import com.couponManagement.service.CouponService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
public class CouponController {

	private final CouponService couponService;

	@Autowired
	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@PostMapping("createCoupon")
	public ResultVO createCoupon(@Valid @RequestBody CreateCouponRequest request) {
		CouponDto coupon = couponService.createCoupon(request);
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupon created successfully", coupon);
	}

	@GetMapping("getAllCoupons")
	public ResultVO getAllCoupons() {
		List<CouponDto> coupons = couponService.getAllCoupons();
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupons retrieved successfully", coupons);
	}

	@GetMapping("coupons/active")
	public ResultVO getActiveCoupons() {
		List<CouponDto> coupons = couponService.getActiveCoupons();
		return new ResultVO(ApiResponseCode.SUCCESS, "Active coupons retrieved successfully", coupons);
	}

	@GetMapping("coupons/{id}")
	public ResultVO getCouponById(@PathVariable Long id) {
		CouponDto coupon = couponService.getCouponById(id);
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupon retrieved successfully", coupon);
	}

	@PutMapping("updateCoupon/{id}")
	public ResultVO updateCoupon(@PathVariable Long id,
			@Valid @RequestBody CreateCouponRequest request) {
		CouponDto coupon = couponService.updateCoupon(id, request);
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupon updated successfully", coupon);
	}

	@DeleteMapping("deleteCoupon/{id}")
	public ResultVO deleteCoupon(@PathVariable Long id) {
		couponService.deleteCoupon(id);
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupon deleted successfully");
	}

	@PostMapping("applicableCoupons")
	public ResultVO getApplicableCoupons(@Valid @RequestBody CartDto cart) {
		ApplicableCouponsResponse response = couponService.getApplicableCoupons(cart);
		return new ResultVO(ApiResponseCode.SUCCESS, "Applicable coupons retrieved successfully", response);
	}

	@PostMapping("applyCoupon/{id}")
	public ResultVO applyCoupon(@PathVariable Long id, @Valid @RequestBody CartDto cart) {
		ApplyCouponResponse response = couponService.applyCoupon(id, cart);
		return new ResultVO(ApiResponseCode.SUCCESS, "Coupon applied successfully", response);
	}
}
