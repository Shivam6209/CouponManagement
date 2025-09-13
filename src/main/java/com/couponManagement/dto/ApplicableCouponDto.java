package com.couponManagement.dto;

import java.math.BigDecimal;

public class ApplicableCouponDto {

    private String couponId;
    private String type;
    private BigDecimal discount;
    private String description;

    public ApplicableCouponDto() {}

    public ApplicableCouponDto(String couponId, String type, BigDecimal discount, String description) {
        this.couponId = couponId;
        this.type = type;
        this.discount = discount;
        this.description = description;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
