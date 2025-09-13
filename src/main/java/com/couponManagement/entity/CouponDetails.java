package com.couponManagement.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class CouponDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "couponId")
    private Coupon coupon;

    public CouponDetails() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public abstract void validate();
}
