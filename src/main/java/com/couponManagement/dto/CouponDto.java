package com.couponManagement.dto;

import java.util.Map;

public class CouponDto {

	private Long id;
	private String couponCode;
	private String type;
	private Map<String, Object> details;
	private boolean active;
	private String createdAt;
	private String updatedAt;

	public CouponDto() {
	}

	public CouponDto(Long id, String couponCode, String type, Map<String, Object> details, boolean active, String createdAt,
			String updatedAt) {
		this.id = id;
		this.couponCode = couponCode;
		this.type = type;
		this.details = details;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
