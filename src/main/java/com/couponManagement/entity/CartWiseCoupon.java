package com.couponManagement.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "cartWiseCoupons")
public class CartWiseCoupon extends CouponDetails {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "threshold", precision = 10, scale = 2)
    private BigDecimal threshold;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    public CartWiseCoupon() {}

    public CartWiseCoupon(BigDecimal threshold, BigDecimal discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void validate() {
        if (threshold == null || threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount must be greater than 0");
        }
    }
}
