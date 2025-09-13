package com.couponManagement.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "bxgyCoupons")
public class BxGyCoupon extends CouponDetails {

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "bxgyBuyProducts", joinColumns = @JoinColumn(name = "bxgyCouponId"))
    @OrderColumn(name = "buyOrder")
    private List<ProductQuantity> buyProducts;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "bxgyGetProducts", joinColumns = @JoinColumn(name = "bxgyCouponId"))
    @OrderColumn(name = "getOrder")
    private List<ProductQuantity> getProducts;

    @NotNull
    @Min(value = 1)
    @Column(name = "repetitionLimit")
    private Integer repetitionLimit;

    public BxGyCoupon() {}

    public List<ProductQuantity> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<ProductQuantity> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public List<ProductQuantity> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(List<ProductQuantity> getProducts) {
        this.getProducts = getProducts;
    }

    public Integer getRepetitionLimit() {
        return repetitionLimit;
    }

    public void setRepetitionLimit(Integer repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }

    public void validate() {
        if (buyProducts == null || buyProducts.isEmpty()) {
            throw new IllegalArgumentException("Buy products list cannot be empty");
        }
        if (getProducts == null || getProducts.isEmpty()) {
            throw new IllegalArgumentException("Get products list cannot be empty");
        }
        if (repetitionLimit == null || repetitionLimit < 1) {
            throw new IllegalArgumentException("Repetition limit must be at least 1");
        }
    }

    @Embeddable
    public static class ProductQuantity {
        @Column(name = "productId", nullable = false)
        private Long productId;

        @Column(name = "quantity", nullable = false)
        private Integer quantity;

        public ProductQuantity() {}

        public ProductQuantity(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
