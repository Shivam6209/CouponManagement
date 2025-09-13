package com.couponManagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class ApplyCouponResponse {

	private List<UpdatedCartItemDto> items;
	private BigDecimal totalPrice;
	private BigDecimal totalDiscount;
	private BigDecimal finalPrice;

	public ApplyCouponResponse() {
	}

	public ApplyCouponResponse(List<UpdatedCartItemDto> items, BigDecimal totalPrice,
			BigDecimal totalDiscount, BigDecimal finalPrice) {
		this.items = items;
		this.totalPrice = totalPrice;
		this.totalDiscount = totalDiscount;
		this.finalPrice = finalPrice;
	}

	public List<UpdatedCartItemDto> getItems() {
		return items;
	}

	public void setItems(List<UpdatedCartItemDto> items) {
		this.items = items;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
}
