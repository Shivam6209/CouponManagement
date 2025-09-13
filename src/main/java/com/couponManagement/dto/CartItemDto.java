package com.couponManagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CartItemDto {

	@NotBlank(message = "Product ID is required")
	private String productId;

	@NotNull(message = "Quantity is required")
	@Positive(message = "Quantity must be positive")
	private Integer quantity;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be positive")
	private BigDecimal price;

	public CartItemDto() {
	}

	public CartItemDto(String productId, Integer quantity, BigDecimal price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
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

	public BigDecimal getTotalPrice() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}
}
