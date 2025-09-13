package com.couponManagement.service;

import com.couponManagement.constants.CouponType;
import com.couponManagement.dto.*;
import com.couponManagement.entity.*;
import com.couponManagement.exception.CouponNotFoundException;
import com.couponManagement.exception.InvalidCouponException;
import com.couponManagement.repository.CouponRepository;
import com.couponManagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository, ProductRepository productRepository) {
        this.couponRepository = couponRepository;
        this.productRepository = productRepository;
    }


    @Override
    public CouponDto createCoupon(CreateCouponRequest request) {
        CouponType couponType;
        try {
            couponType = CouponType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCouponException("Invalid coupon type: " + request.getType());
        }

        Coupon coupon = new Coupon(generateCouponCode(), couponType);

        // Create appropriate detail entity based on coupon type
        switch (couponType) {
            case CART_WISE:
                createCartWiseCoupon(coupon, request.getCartWiseDetails());
                break;
            case PRODUCT_WISE:
                createProductWiseCoupon(coupon, request.getProductWiseDetails());
                break;
            case BXGY:
                createBxGyCoupon(coupon, request.getBxGyDetails());
                break;
        }

        coupon = couponRepository.save(coupon);

        return convertToDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDto getCouponById(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithDetails(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId.toString()));
        return convertToDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAllWithDetails().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getActiveCoupons() {
        return couponRepository.findByIsActiveTrueWithDetails().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CouponDto updateCoupon(Long couponId, CreateCouponRequest request) {
        Coupon coupon = couponRepository.findByIdWithDetails(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId.toString()));

        CouponType couponType;
        try {
            couponType = CouponType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCouponException("Invalid coupon type: " + request.getType());
        }

        coupon.setCouponType(couponType);

        coupon.setCartWiseDetails(null);
        coupon.setProductWiseDetails(null);
        coupon.setBxGyDetails(null);

        // Create new appropriate detail entity based on coupon type
        switch (couponType) {
            case CART_WISE:
                createCartWiseCoupon(coupon, request.getCartWiseDetails());
                break;
            case PRODUCT_WISE:
                createProductWiseCoupon(coupon, request.getProductWiseDetails());
                break;
            case BXGY:
                createBxGyCoupon(coupon, request.getBxGyDetails());
                break;
        }

        coupon.setIsActive(true);

        coupon = couponRepository.save(coupon);
        return convertToDto(coupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithDetails(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId.toString()));

        coupon.setIsActive(false);
        couponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicableCouponsResponse getApplicableCoupons(CartDto cart) {
        List<ApplicableCouponDto> applicableCoupons = new ArrayList<>();

        List<Coupon> activeCoupons = couponRepository.findByIsActiveTrueWithDetails();

        Map<Long, Integer> cartProductQuantities = cart.getItems().stream()
                .collect(Collectors.toMap(
                        item -> Long.valueOf(item.getProductId()),
                        CartItemDto::getQuantity
                ));

        Map<String, BigDecimal> cartPrices = cart.getItems().stream()
                .collect(Collectors.toMap(
                        CartItemDto::getProductId,
                        CartItemDto::getPrice
                ));

        BigDecimal cartTotal = cart.getTotalPrice();

        for (Coupon coupon : activeCoupons) {
            ApplicableCouponDto applicableCoupon = null;

            switch (coupon.getCouponType()) {
                case CART_WISE:
                    applicableCoupon = checkCartWiseApplicability(coupon, cartTotal);
                    break;
                case PRODUCT_WISE:
                    applicableCoupon = checkProductWiseApplicability(coupon, cartProductQuantities, cartPrices);
                    break;
                case BXGY:
                    applicableCoupon = checkBxGyApplicability(coupon, cartProductQuantities);
                    break;
            }

            if (applicableCoupon != null) {
                applicableCoupons.add(applicableCoupon);
            }
        }

        return new ApplicableCouponsResponse(applicableCoupons);
    }

    private ApplicableCouponDto checkCartWiseApplicability(Coupon coupon, BigDecimal cartTotal) {
        CartWiseCoupon cartWise = coupon.getCartWiseDetails();
        if (cartWise != null && cartTotal.compareTo(cartWise.getThreshold()) >= 0) {
            BigDecimal discountAmount = cartTotal.multiply(cartWise.getDiscount()).divide(BigDecimal.valueOf(100));
            return new ApplicableCouponDto(
                    coupon.getCouponCode(),
                    coupon.getCouponType().toString(),
                    discountAmount,
                    "Get " + cartWise.getDiscount() + "% off on cart total of " + cartTotal + " (₹" + discountAmount + ")"
            );
        }
        return null;
    }

    private ApplicableCouponDto checkProductWiseApplicability(Coupon coupon, Map<Long, Integer> cartProducts, Map<String, BigDecimal> cartPrices) {
        ProductWiseCoupon productWise = coupon.getProductWiseDetails();
        if (productWise != null && cartProducts.containsKey(productWise.getProductId())) {
            Integer quantity = cartProducts.get(productWise.getProductId());
            BigDecimal discountAmount = BigDecimal.ZERO;

            if (quantity != null) {
                BigDecimal itemPrice = cartPrices.get(productWise.getProductId().toString());
                if (itemPrice != null) {
                    // discount = (price * quantity * percentage) / 100
                    BigDecimal totalItemPrice = itemPrice.multiply(BigDecimal.valueOf(quantity));
                    discountAmount = totalItemPrice.multiply(productWise.getDiscount()).divide(BigDecimal.valueOf(100));
                }
            }

            return new ApplicableCouponDto(
                    coupon.getCouponCode(),
                    coupon.getCouponType().toString(),
                    discountAmount,
                    "Get " + productWise.getDiscount() + "% off on product " + productWise.getProductId() + " - Save ₹" + discountAmount
            );
        }
        return null;
    }

    private ApplicableCouponDto checkBxGyApplicability(Coupon coupon, Map<Long, Integer> cartProducts) {
        BxGyCoupon bxGy = coupon.getBxGyDetails();
        if (bxGy == null) return null;

        boolean canApply = true;
        int maxFreeItems = 0;

        for (BxGyCoupon.ProductQuantity buyProduct : bxGy.getBuyProducts()) {
            Integer cartQuantity = cartProducts.get(buyProduct.getProductId());
            if (cartQuantity == null || cartQuantity < buyProduct.getQuantity()) {
                canApply = false;
                break;
            }
            // Calculate how many times we can apply this.
            int timesApplicable = cartQuantity / buyProduct.getQuantity();
            if (maxFreeItems == 0 || timesApplicable < maxFreeItems) {
                maxFreeItems = timesApplicable;
            }
        }

        if (canApply && maxFreeItems > 0) {
            int actualFreeItems = Math.min(maxFreeItems, bxGy.getRepetitionLimit());

            Map<Long, Product> productMap = getProductMap(
                    bxGy.getGetProducts().stream()
                            .map(BxGyCoupon.ProductQuantity::getProductId)
                            .collect(Collectors.toList())
            );

            BigDecimal totalDiscount = BigDecimal.ZERO;
            for (BxGyCoupon.ProductQuantity getProduct : bxGy.getGetProducts()) {
                Product product = productMap.get(getProduct.getProductId());
                if (product != null) {
                    Integer cartQuantity = cartProducts.get(getProduct.getProductId());
                    if (cartQuantity != null) {
                        int freeQuantity = Math.min(actualFreeItems * getProduct.getQuantity(), cartQuantity);
                        // Add the value of free items to total discount
                        totalDiscount = totalDiscount.add(product.getPrice().multiply(BigDecimal.valueOf(freeQuantity)));
                    }
                }
            }

            return new ApplicableCouponDto(
                    coupon.getCouponCode(),
                    coupon.getCouponType().toString(),
                    totalDiscount,
                    "Buy " + bxGy.getBuyProducts().size() + " get " + bxGy.getGetProducts().size() + " free (up to " + bxGy.getRepetitionLimit() + " times) - Save ₹" + totalDiscount
            );
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ApplyCouponResponse applyCoupon(Long couponId, CartDto cart) {
        Coupon coupon = couponRepository.findByIdWithDetails(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId.toString()));

        if (!coupon.getIsActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        List<UpdatedCartItemDto> updatedItems = new ArrayList<>();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        switch (coupon.getCouponType()) {
            case CART_WISE:
                updatedItems = applyCartWiseCoupon(coupon, cart);
                BigDecimal cartTotal = updatedItems.stream()
                        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                totalDiscount = cartTotal.multiply(coupon.getCartWiseDetails().getDiscount()).divide(BigDecimal.valueOf(100));
                break;
            case PRODUCT_WISE:
                updatedItems = applyProductWiseCoupon(coupon, cart);
                totalDiscount = calculateProductWiseDiscount(updatedItems);
                break;
            case BXGY:
                updatedItems = applyBxGyCoupon(coupon, cart);
                totalDiscount = calculateBxGyDiscount(updatedItems, cart.getItems());
                break;
        }

        // original total price (before discount)
        BigDecimal originalTotalPrice = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //final price (after discount)
        BigDecimal finalPrice = originalTotalPrice.subtract(totalDiscount);

        return new ApplyCouponResponse(updatedItems, originalTotalPrice, totalDiscount, finalPrice);
    }

	private List<UpdatedCartItemDto> applyCartWiseCoupon(Coupon coupon, CartDto cart) {
		return cart.getItems().stream().map(item -> {
			return new UpdatedCartItemDto(item.getProductId(), item.getQuantity(), item.getPrice(),
					BigDecimal.ZERO);
		}).collect(Collectors.toList());
	}

    private List<UpdatedCartItemDto> applyProductWiseCoupon(Coupon coupon, CartDto cart) {
        ProductWiseCoupon productWise = coupon.getProductWiseDetails();
        Long targetProductId = productWise.getProductId();
        BigDecimal discountPercentage = productWise.getDiscount();

        return cart.getItems().stream()
                .map(item -> {
                    BigDecimal itemDiscount = BigDecimal.ZERO;
                    if (item.getProductId().equals(targetProductId.toString())) {
                        // Calculate actual discount amount: (price * quantity * percentage) / 100
                        BigDecimal totalItemPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        itemDiscount = totalItemPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
                    }
                    return new UpdatedCartItemDto(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getPrice(),
                            itemDiscount
                    );
                })
                .collect(Collectors.toList());
    }

    private List<UpdatedCartItemDto> applyBxGyCoupon(Coupon coupon, CartDto cart) {
        BxGyCoupon bxGy = coupon.getBxGyDetails();

        Map<String, CartItemDto> cartItemMap = cart.getItems().stream()
                .collect(Collectors.toMap(CartItemDto::getProductId, item -> item));

        // Calculate how many free items we can give
        int maxFreeItems = calculateMaxFreeItems(bxGy, cartItemMap);

        // Apply the free items to getProducts
        List<UpdatedCartItemDto> updatedItems = new ArrayList<>();

        for (CartItemDto item : cart.getItems()) {
            int originalQuantity = item.getQuantity();
            int additionalFreeQuantity = 0;

            for (BxGyCoupon.ProductQuantity getProduct : bxGy.getGetProducts()) {
                if (item.getProductId().equals(getProduct.getProductId().toString())) {
                    additionalFreeQuantity = Math.min(maxFreeItems * getProduct.getQuantity(), originalQuantity);
                    break;
                }
            }

			updatedItems.add(new UpdatedCartItemDto(item.getProductId(), originalQuantity + additionalFreeQuantity,
					item.getPrice(), BigDecimal.ZERO));
        }

        return updatedItems;
    }

    private int calculateMaxFreeItems(BxGyCoupon bxGy, Map<String, CartItemDto> cartItems) {
        int maxFreeItems = Integer.MAX_VALUE;

        for (BxGyCoupon.ProductQuantity buyProduct : bxGy.getBuyProducts()) {
            CartItemDto cartItem = cartItems.get(buyProduct.getProductId().toString());
            if (cartItem == null) {
                return 0; // Can't apply if any buy product is missing
            }

            int timesApplicable = cartItem.getQuantity() / buyProduct.getQuantity();
            maxFreeItems = Math.min(maxFreeItems, timesApplicable);
        }

        return Math.min(maxFreeItems, bxGy.getRepetitionLimit());
    }

    private BigDecimal calculateProductWiseDiscount(List<UpdatedCartItemDto> updatedItems) {
        return updatedItems.stream()
                .map(UpdatedCartItemDto::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBxGyDiscount(List<UpdatedCartItemDto> updatedItems, List<CartItemDto> originalItems) {
        // For BxGy, the discount is the value of the free items added
        BigDecimal discount = BigDecimal.ZERO;

        Map<String, CartItemDto> originalItemMap = originalItems.stream()
                .collect(Collectors.toMap(CartItemDto::getProductId, item -> item));

        Map<Long, Product> productMap = getProductMap(
                updatedItems.stream()
                        .filter(updatedItem -> {
                            CartItemDto originalItem = originalItemMap.get(updatedItem.getProductId());
                            return originalItem != null && updatedItem.getQuantity() > originalItem.getQuantity();
                        })
                        .map(updatedItem -> Long.valueOf(updatedItem.getProductId()))
                        .distinct()
                        .collect(Collectors.toList())
        );

        for (UpdatedCartItemDto updatedItem : updatedItems) {
            CartItemDto originalItem = originalItemMap.get(updatedItem.getProductId());
            if (originalItem != null) {
                int freeQuantity = updatedItem.getQuantity() - originalItem.getQuantity();
                if (freeQuantity > 0) {
                    Product product = productMap.get(Long.valueOf(originalItem.getProductId()));
                    if (product != null) {
                        discount = discount.add(product.getPrice().multiply(BigDecimal.valueOf(freeQuantity)));
                    }
                }
            }
        }

        return discount;
    }

    private Map<Long, Product> getProductMap(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new HashMap<>();
        }
        List<Product> products = productRepository.findAllById(productIds);
        return products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private void createCartWiseCoupon(Coupon coupon, CartWiseCouponRequest details) {
        if (details == null) {
            throw new InvalidCouponException("Cart-wise coupon details are required");
        }

        CartWiseCoupon cartWiseCoupon = new CartWiseCoupon(details.getThreshold(), details.getDiscount());
        cartWiseCoupon.validate();
        coupon.setCartWiseDetails(cartWiseCoupon);
    }

    private void createProductWiseCoupon(Coupon coupon, ProductWiseCouponRequest details) {
        if (details == null) {
            throw new InvalidCouponException("Product-wise coupon details are required");
        }

        ProductWiseCoupon productWiseCoupon = new ProductWiseCoupon(details.getProductId(), details.getDiscount());
        productWiseCoupon.validate();
        coupon.setProductWiseDetails(productWiseCoupon);
    }

    private void createBxGyCoupon(Coupon coupon, BxGyCouponRequest details) {
        if (details == null) {
            throw new InvalidCouponException("BxGy coupon details are required");
        }

        List<BxGyCoupon.ProductQuantity> buyProducts = details.getBuyProducts().stream()
                .map(pq -> new BxGyCoupon.ProductQuantity(pq.getProductId(), pq.getQuantity()))
                .collect(Collectors.toList());

        List<BxGyCoupon.ProductQuantity> getProducts = details.getGetProducts().stream()
                .map(pq -> new BxGyCoupon.ProductQuantity(pq.getProductId(), pq.getQuantity()))
                .collect(Collectors.toList());

        BxGyCoupon bxGyCoupon = new BxGyCoupon();
        bxGyCoupon.setBuyProducts(buyProducts);
        bxGyCoupon.setGetProducts(getProducts);
        bxGyCoupon.setRepetitionLimit(details.getRepetitionLimit());

        bxGyCoupon.validate();
        coupon.setBxGyDetails(bxGyCoupon);
    }



    private String generateCouponCode() {
        return "CPN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private CouponDto convertToDto(Coupon coupon) {
        Map<String, Object> details = extractDetailsFromEntity(coupon);
        return new CouponDto(
                coupon.getId(),
                coupon.getCouponCode(),
                coupon.getCouponType().toString(),
                details,
                coupon.getIsActive(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }

    private Map<String, Object> extractDetailsFromEntity(Coupon coupon) {
        Map<String, Object> details = new HashMap<>();

        switch (coupon.getCouponType()) {
            case CART_WISE:
                CartWiseCoupon cartWise = coupon.getCartWiseDetails();
                if (cartWise != null) {
                    details.put("threshold", cartWise.getThreshold());
                    details.put("discount", cartWise.getDiscount());
                }
                break;
            case PRODUCT_WISE:
                ProductWiseCoupon productWise = coupon.getProductWiseDetails();
                if (productWise != null) {
                    details.put("productId", productWise.getProductId());
                    details.put("discount", productWise.getDiscount());
                }
                break;
            case BXGY:
                BxGyCoupon bxGy = coupon.getBxGyDetails();
                if (bxGy != null) {
                    details.put("buyProducts", bxGy.getBuyProducts());
                    details.put("getProducts", bxGy.getGetProducts());
                    details.put("repitionLimit", bxGy.getRepetitionLimit());
                }
                break;
        }

        return details;
    }
}
