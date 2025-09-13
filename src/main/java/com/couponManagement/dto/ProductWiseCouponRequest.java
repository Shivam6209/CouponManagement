package com.couponManagement.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductWiseCouponRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0")
    private BigDecimal discount;

    public ProductWiseCouponRequest() {}

    public ProductWiseCouponRequest(Long productId, BigDecimal discount) {
        this.productId = productId;
        this.discount = discount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
