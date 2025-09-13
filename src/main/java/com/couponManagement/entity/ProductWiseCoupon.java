package com.couponManagement.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "productWiseCoupons")
public class ProductWiseCoupon extends CouponDetails {

    @NotNull
    @Column(name = "productId", nullable = false)
    private Long productId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    public ProductWiseCoupon() {}

    public ProductWiseCoupon(Long productId, BigDecimal discount) {
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

    public void validate() {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be valid");
        }
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount must be greater than 0");
        }
    }
}
