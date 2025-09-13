package com.couponManagement.service;

import com.couponManagement.dto.CreateProductRequest;
import com.couponManagement.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest request);
    ProductDto getProductById(Long productId);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(Long productId, CreateProductRequest request);
    void deleteProduct(Long productId);

    List<ProductDto> getProductsByCategory(String category);
    List<ProductDto> searchProducts(String keyword);
    List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    boolean productExists(Long productId);
}
