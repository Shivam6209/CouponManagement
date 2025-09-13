package com.couponManagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateCouponRequest {

    @NotBlank(message = "Coupon type is required")
    private String type; // CART_WISE, PRODUCT_WISE, BXGY

    private CartWiseCouponRequest cartWiseDetails;
    private ProductWiseCouponRequest productWiseDetails;
    private BxGyCouponRequest bxGyDetails;

    public CreateCouponRequest() {}

    public CreateCouponRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CartWiseCouponRequest getCartWiseDetails() {
        return cartWiseDetails;
    }

    public void setCartWiseDetails(CartWiseCouponRequest cartWiseDetails) {
        this.cartWiseDetails = cartWiseDetails;
    }

    public ProductWiseCouponRequest getProductWiseDetails() {
        return productWiseDetails;
    }

    public void setProductWiseDetails(ProductWiseCouponRequest productWiseDetails) {
        this.productWiseDetails = productWiseDetails;
    }

    public BxGyCouponRequest getBxGyDetails() {
        return bxGyDetails;
    }

    public void setBxGyDetails(BxGyCouponRequest bxGyDetails) {
        this.bxGyDetails = bxGyDetails;
    }

    @NotNull(message = "Coupon details are required")
    public Object getDetails() {
        switch (type != null ? type.toUpperCase() : "") {
            case "CART_WISE":
                return cartWiseDetails;
            case "PRODUCT_WISE":
                return productWiseDetails;
            case "BXGY":
                return bxGyDetails;
            default:
                return null;
        }
    }
}
