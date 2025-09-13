package com.couponManagement.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CartWiseCouponRequest {

    @NotNull(message = "Threshold is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Threshold must be greater than 0")
    private BigDecimal threshold;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    private BigDecimal discount;

    public CartWiseCouponRequest() {}

    public CartWiseCouponRequest(BigDecimal threshold, BigDecimal discount) {
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
}
