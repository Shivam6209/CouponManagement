package com.couponManagement.dto;

import java.util.List;

public class ApplicableCouponsResponse {

	private List<ApplicableCouponDto> applicableCoupons;

	public ApplicableCouponsResponse() {
	}

	public ApplicableCouponsResponse(List<ApplicableCouponDto> applicableCoupons) {
		this.applicableCoupons = applicableCoupons;
	}

	public List<ApplicableCouponDto> getApplicableCoupons() {
		return applicableCoupons;
	}

	public void setApplicableCoupons(List<ApplicableCouponDto> applicableCoupons) {
		this.applicableCoupons = applicableCoupons;
	}
}
