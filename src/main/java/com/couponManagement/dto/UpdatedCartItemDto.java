package com.couponManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class UpdatedCartItemDto {

    private String productId;
    private Integer quantity;
    private BigDecimal price;

    @JsonProperty("totalDiscount")
    private BigDecimal discount;
    
    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;

    public UpdatedCartItemDto() {}

    public UpdatedCartItemDto(String productId, Integer quantity, BigDecimal price, BigDecimal discount) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity)).subtract(this.discount); // Final price after discount
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
