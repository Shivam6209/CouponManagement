package com.couponManagement.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BxGyCouponRequest {

    @NotEmpty(message = "Buy products list cannot be empty")
    private List<BuyGetProduct> buyProducts;

    @NotEmpty(message = "Get products list cannot be empty")
    private List<BuyGetProduct> getProducts;

    @NotNull(message = "Repetition limit is required")
    @Min(value = 1, message = "Repetition limit must be at least 1")
    private Integer repetitionLimit;

    public BxGyCouponRequest() {}

    public BxGyCouponRequest(List<BuyGetProduct> buyProducts, List<BuyGetProduct> getProducts, Integer repetitionLimit) {
        this.buyProducts = buyProducts;
        this.getProducts = getProducts;
        this.repetitionLimit = repetitionLimit;
    }

    public List<BuyGetProduct> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<BuyGetProduct> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public List<BuyGetProduct> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(List<BuyGetProduct> getProducts) {
        this.getProducts = getProducts;
    }

    public Integer getRepetitionLimit() {
        return repetitionLimit;
    }

    public void setRepetitionLimit(Integer repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }

    public static class BuyGetProduct {
        private Long productId;
        private Integer quantity;

        public BuyGetProduct() {}

        public BuyGetProduct(Long productId, Integer quantity) {
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
