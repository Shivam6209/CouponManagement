package com.couponManagement.controller;

import com.couponManagement.VO.ResultVO;
import com.couponManagement.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class CouponControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateAndGetCoupon() throws Exception {
        // Create a cart-wise coupon
        CreateCouponRequest createRequest = new CreateCouponRequest();
        createRequest.setType("cart_wise");

        CartWiseCouponRequest cartWiseDetails = new CartWiseCouponRequest();
        cartWiseDetails.setThreshold(BigDecimal.valueOf(500.0));
        cartWiseDetails.setDiscount(BigDecimal.valueOf(10.0));
        createRequest.setCartWiseDetails(cartWiseDetails);

        String createJson = objectMapper.writeValueAsString(createRequest);

       String createResponse = mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon created successfully"))
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        CouponDto createdCoupon = objectMapper.convertValue(createResult.getResult(), CouponDto.class);
        Long couponId = createdCoupon.getId();

        mockMvc.perform(get("/api/coupons/" + couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon retrieved successfully"))
                .andExpect(jsonPath("$.result.id").value(couponId));
    }

    @Test
    void testGetAllCoupons() throws Exception {
        mockMvc.perform(get("/api/getAllCoupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupons retrieved successfully"));
    }

    @Test
    void testGetActiveCoupons() throws Exception {
        mockMvc.perform(get("/api/coupons/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Active coupons retrieved successfully"));
    }

    @Test
    void testApplicableCoupons() throws Exception {
        // First create a cart-wise coupon
        CreateCouponRequest cartWiseRequest = new CreateCouponRequest();
        cartWiseRequest.setType("cart_wise");

        CartWiseCouponRequest cartWiseDetails = new CartWiseCouponRequest();
        cartWiseDetails.setThreshold(BigDecimal.valueOf(100.0));
        cartWiseDetails.setDiscount(BigDecimal.valueOf(10.0)); // 10% discount
        cartWiseRequest.setCartWiseDetails(cartWiseDetails);

        String cartWiseJson = objectMapper.writeValueAsString(cartWiseRequest);

        // Create cart-wise coupon
        mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartWiseJson))
                .andExpect(status().isOk());

        // Create a product-wise coupon
        CreateCouponRequest productWiseRequest = new CreateCouponRequest();
        productWiseRequest.setType("product_wise");

        ProductWiseCouponRequest productWiseDetails = new ProductWiseCouponRequest();
        productWiseDetails.setProductId(1L);
        productWiseDetails.setDiscount(BigDecimal.valueOf(20.0)); // 20% discount
        productWiseRequest.setProductWiseDetails(productWiseDetails);

        String productWiseJson = objectMapper.writeValueAsString(productWiseRequest);

        mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productWiseJson))
                .andExpect(status().isOk());

        // Test applicable coupons for cart
        CartDto cart = new CartDto(Arrays.asList(
            new CartItemDto("1", 6, BigDecimal.valueOf(50.0)), // Product 1: 6 * 50 = 300
            new CartItemDto("2", 3, BigDecimal.valueOf(30.0)), // Product 2: 3 * 30 = 90
            new CartItemDto("3", 2, BigDecimal.valueOf(25.0))  // Product 3: 2 * 25 = 50
        ));
        // Total cart value: 300 + 90 + 50 = 440

        String cartJson = objectMapper.writeValueAsString(cart);

        String applicableResponse = mockMvc.perform(post("/api/applicableCoupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Applicable coupons retrieved successfully"))
                .andReturn().getResponse().getContentAsString();

        // Assert exact discount calculations
        ApplicableCouponsResponse applicableResult = objectMapper.convertValue(
            objectMapper.readValue(applicableResponse, ResultVO.class).getResult(),
            ApplicableCouponsResponse.class
        );

        // Cart-wise coupon should offer 10% of 440 = 44.0 discount
        ApplicableCouponDto cartWiseApplicable = applicableResult.getApplicableCoupons().stream()
            .filter(coupon -> coupon.getType().equals("CART_WISE"))
            .findFirst().orElse(null);
        assertNotNull(cartWiseApplicable, "Cart-wise coupon should be applicable");
        assertEquals(BigDecimal.valueOf(44.0), cartWiseApplicable.getDiscount(),
            "Cart-wise discount should be 10% of 440 = 44.0");

        // Product-wise coupon should offer 20% of (6 * 50) = 60.0 discount
        ApplicableCouponDto productWiseApplicable = applicableResult.getApplicableCoupons().stream()
            .filter(coupon -> coupon.getType().equals("PRODUCT_WISE"))
            .findFirst().orElse(null);
        assertNotNull(productWiseApplicable, "Product-wise coupon should be applicable");
        assertEquals(BigDecimal.valueOf(60.0), productWiseApplicable.getDiscount(),
            "Product-wise discount should be 20% of 300 = 60.0");
    }

    @Test
    void testUpdateCoupon() throws Exception {
        // First create a coupon
        CreateCouponRequest createRequest = new CreateCouponRequest();
        createRequest.setType("cart_wise");

        CartWiseCouponRequest cartWiseDetails = new CartWiseCouponRequest();
        cartWiseDetails.setThreshold(BigDecimal.valueOf(300.0));
        cartWiseDetails.setDiscount(BigDecimal.valueOf(5.0));
        createRequest.setCartWiseDetails(cartWiseDetails);

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        CouponDto createdCoupon = objectMapper.convertValue(createResult.getResult(), CouponDto.class);
        Long couponId = createdCoupon.getId();

        // Update the coupon
        CreateCouponRequest updateRequest = new CreateCouponRequest();
        updateRequest.setType("cart_wise");

        CartWiseCouponRequest updatedDetails = new CartWiseCouponRequest();
        updatedDetails.setThreshold(BigDecimal.valueOf(400.0));
        updatedDetails.setDiscount(BigDecimal.valueOf(8.0));
        updateRequest.setCartWiseDetails(updatedDetails);

        String updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/updateCoupon/" + couponId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon updated successfully"));
    }

    @Test
    void testDeleteCoupon() throws Exception {
        // First create a coupon
        CreateCouponRequest createRequest = new CreateCouponRequest();
        createRequest.setType("cart_wise");

        CartWiseCouponRequest cartWiseDetails = new CartWiseCouponRequest();
        cartWiseDetails.setThreshold(BigDecimal.valueOf(200.0));
        cartWiseDetails.setDiscount(BigDecimal.valueOf(5.0));
        createRequest.setCartWiseDetails(cartWiseDetails);

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        CouponDto createdCoupon = objectMapper.convertValue(createResult.getResult(), CouponDto.class);
        Long couponId = createdCoupon.getId();

        // Delete the coupon
        mockMvc.perform(delete("/api/deleteCoupon/" + couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon deleted successfully"));
    }

    @Test
    void testApplyCartWiseCoupon() throws Exception {
        // Create a cart-wise coupon: 10% discount above 100 threshold
        CreateCouponRequest createRequest = new CreateCouponRequest();
        createRequest.setType("cart_wise");

        CartWiseCouponRequest cartWiseDetails = new CartWiseCouponRequest();
        cartWiseDetails.setThreshold(BigDecimal.valueOf(100.0));
        cartWiseDetails.setDiscount(BigDecimal.valueOf(10.0)); // 10% discount
        createRequest.setCartWiseDetails(cartWiseDetails);

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        CouponDto createdCoupon = objectMapper.convertValue(createResult.getResult(), CouponDto.class);
        Long couponId = createdCoupon.getId();

        // Apply coupon to cart
        CartDto cart = new CartDto(Arrays.asList(
            new CartItemDto("1", 2, BigDecimal.valueOf(50.0)), // Item 1: 2 * 50 = 100
            new CartItemDto("2", 1, BigDecimal.valueOf(75.0))  // Item 2: 1 * 75 = 75
        ));
        // Total cart value: 100 + 75 = 175

        String cartJson = objectMapper.writeValueAsString(cart);

        String applyResponse = mockMvc.perform(post("/api/applyCoupon/" + couponId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon applied successfully"))
                .andReturn().getResponse().getContentAsString();

        // Assert exact discount calculations and cart updates
        ApplyCouponResponse applyResult = objectMapper.convertValue(
            objectMapper.readValue(applyResponse, ResultVO.class).getResult(),
            ApplyCouponResponse.class
        );

        // Verify cart calculations
        assertEquals(BigDecimal.valueOf(175.0), applyResult.getTotalPrice(),
            "Total price should be 175.0");
        assertEquals(BigDecimal.valueOf(17.5), applyResult.getTotalDiscount(),
            "Total discount should be 10% of 175 = 17.5");
        assertEquals(BigDecimal.valueOf(157.5), applyResult.getFinalPrice(),
            "Final price should be 175 - 17.5 = 157.5");

        // Verify individual cart items remain unchanged for cart-wise coupon
        assertEquals(2, applyResult.getItems().size(), "Should have 2 items");

        // Find items by product ID
        UpdatedCartItemDto item1 = applyResult.getItems().stream()
            .filter(item -> item.getProductId().equals("1"))
            .findFirst().orElse(null);
        UpdatedCartItemDto item2 = applyResult.getItems().stream()
            .filter(item -> item.getProductId().equals("2"))
            .findFirst().orElse(null);

        assertNotNull(item1, "Item 1 should be present");
        assertNotNull(item2, "Item 2 should be present");

        // For cart-wise coupons, items should have no individual discount
        assertEquals(2, item1.getQuantity(), "Item 1 quantity should remain 2");
        assertEquals(BigDecimal.valueOf(50.0), item1.getPrice(), "Item 1 price should remain 50.0");
        assertEquals(BigDecimal.ZERO, item1.getDiscount(), "Item 1 discount should be 0 for cart-wise");

        assertEquals(1, item2.getQuantity(), "Item 2 quantity should remain 1");
        assertEquals(BigDecimal.valueOf(75.0), item2.getPrice(), "Item 2 price should remain 75.0");
        assertEquals(BigDecimal.ZERO, item2.getDiscount(), "Item 2 discount should be 0 for cart-wise");
    }

    @Test
    void testApplyProductWiseCoupon() throws Exception {
        // Create a product-wise coupon: 20% discount on product 1
        CreateCouponRequest createRequest = new CreateCouponRequest();
        createRequest.setType("product_wise");

        ProductWiseCouponRequest productWiseDetails = new ProductWiseCouponRequest();
        productWiseDetails.setProductId(1L);
        productWiseDetails.setDiscount(BigDecimal.valueOf(20.0)); // 20% discount
        createRequest.setProductWiseDetails(productWiseDetails);

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/createCoupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        CouponDto createdCoupon = objectMapper.convertValue(createResult.getResult(), CouponDto.class);
        Long couponId = createdCoupon.getId();

        // Apply coupon to cart
        CartDto cart = new CartDto(Arrays.asList(
            new CartItemDto("1", 3, BigDecimal.valueOf(50.0)), // Product 1: 3 * 50 = 150 (should get 20% discount)
            new CartItemDto("2", 2, BigDecimal.valueOf(30.0))  // Product 2: 2 * 30 = 60 (no discount)
        ));

        String cartJson = objectMapper.writeValueAsString(cart);

        String applyResponse = mockMvc.perform(post("/api/applyCoupon/" + couponId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Coupon applied successfully"))
                .andReturn().getResponse().getContentAsString();

        // Assert exact discount calculations
        ApplyCouponResponse applyResult = objectMapper.convertValue(
            objectMapper.readValue(applyResponse, ResultVO.class).getResult(),
            ApplyCouponResponse.class
        );

        // Verify cart calculations
        assertEquals(BigDecimal.valueOf(210.0), applyResult.getTotalPrice(),
            "Total price should be 210.0");
        assertEquals(BigDecimal.valueOf(30.0), applyResult.getTotalDiscount(),
            "Total discount should be 20% of 150 = 30.0");
        assertEquals(BigDecimal.valueOf(180.0), applyResult.getFinalPrice(),
            "Final price should be 210 - 30 = 180.0");

        // Verify individual cart items
        UpdatedCartItemDto item1 = applyResult.getItems().stream()
            .filter(item -> item.getProductId().equals("1"))
            .findFirst().orElse(null);
        UpdatedCartItemDto item2 = applyResult.getItems().stream()
            .filter(item -> item.getProductId().equals("2"))
            .findFirst().orElse(null);

        // Product 1 should have discount
        assertNotNull(item1, "Item 1 should be present");
        assertEquals(3, item1.getQuantity(), "Item 1 quantity should remain 3");
        assertEquals(BigDecimal.valueOf(50.0), item1.getPrice(), "Item 1 price should remain 50.0");
        assertEquals(BigDecimal.valueOf(30.0), item1.getDiscount(),
            "Item 1 discount should be 20% of 150 = 30.0");

        // Product 2 should have no discount
        assertNotNull(item2, "Item 2 should be present");
        assertEquals(2, item2.getQuantity(), "Item 2 quantity should remain 2");
        assertEquals(BigDecimal.valueOf(30.0), item2.getPrice(), "Item 2 price should remain 30.0");
        assertEquals(BigDecimal.ZERO, item2.getDiscount(),
            "Item 2 discount should be 0 for product-wise coupon on different product");
    }
}
