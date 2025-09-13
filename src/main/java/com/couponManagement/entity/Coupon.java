package com.couponManagement.entity;

import com.couponManagement.constants.CouponType;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "coupons")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "couponCode", unique = true, nullable = false)
	private String couponCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "couponType", nullable = false)
	private CouponType couponType;

	@OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
	private CartWiseCoupon cartWiseDetails;

	@OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
	private ProductWiseCoupon productWiseDetails;

	@OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
	private BxGyCoupon bxGyDetails;

	@Column(name = "isActive")
	private Boolean isActive = true;

	@Column(name = "createdAt")
	private String createdAt;

	@Column(name = "updatedAt")
	private String updatedAt;

	public Coupon() {
	}

	public Coupon(String couponCode, CouponType couponType) {
		this.couponCode = couponCode;
		this.couponType = couponType;
		this.isActive = true;
		this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
		this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
		updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public CouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}

	public CartWiseCoupon getCartWiseDetails() {
		return cartWiseDetails;
	}

	public void setCartWiseDetails(CartWiseCoupon cartWiseDetails) {
		this.cartWiseDetails = cartWiseDetails;
		if (cartWiseDetails != null) {
			cartWiseDetails.setCoupon(this);
		}
	}

	public ProductWiseCoupon getProductWiseDetails() {
		return productWiseDetails;
	}

	public void setProductWiseDetails(ProductWiseCoupon productWiseDetails) {
		this.productWiseDetails = productWiseDetails;
		if (productWiseDetails != null) {
			productWiseDetails.setCoupon(this);
		}
	}

	public BxGyCoupon getBxGyDetails() {
		return bxGyDetails;
	}

	public void setBxGyDetails(BxGyCoupon bxGyDetails) {
		this.bxGyDetails = bxGyDetails;
		if (bxGyDetails != null) {
			bxGyDetails.setCoupon(this);
		}
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
