package com.couponManagement.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

public class CartDto {

	@NotEmpty(message = "Cart items cannot be empty")
	@Valid
	private List<CartItemDto> items;

	public CartDto() {
	}

	public CartDto(List<CartItemDto> items) {
		this.items = items;
	}

	public List<CartItemDto> getItems() {
		return items;
	}

	public void setItems(List<CartItemDto> items) {
		this.items = items;
	}

	public BigDecimal getTotalPrice() {
		return items.stream().map(CartItemDto::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
