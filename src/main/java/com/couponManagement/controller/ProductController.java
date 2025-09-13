package com.couponManagement.controller;

import com.couponManagement.constants.ApiResponseCode;
import com.couponManagement.dto.CreateProductRequest;
import com.couponManagement.dto.ProductDto;
import com.couponManagement.VO.ResultVO;
import com.couponManagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResultVO createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductDto product = productService.createProduct(request);
        return new ResultVO(ApiResponseCode.SUCCESS, "Product created successfully", product);
    }

    @GetMapping("/{id}")
    public ResultVO getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return new ResultVO(ApiResponseCode.SUCCESS, "Product retrieved successfully", product);
    }

    @GetMapping
    public ResultVO getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResultVO(ApiResponseCode.SUCCESS, "Products retrieved successfully", products);
    }

    @PutMapping("/{id}")
    public ResultVO updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        ProductDto product = productService.updateProduct(id, request);
        return new ResultVO(ApiResponseCode.SUCCESS, "Product updated successfully", product);
    }

    @DeleteMapping("/{id}")
    public ResultVO deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResultVO(ApiResponseCode.SUCCESS, "Product deleted successfully");
    }

    @GetMapping("/category/{category}")
    public ResultVO getProductsByCategory(@PathVariable String category) {
        List<ProductDto> products = productService.getProductsByCategory(category);
        return new ResultVO(ApiResponseCode.SUCCESS, "Products retrieved by category successfully", products);
    }

    @GetMapping("/search")
    public ResultVO searchProducts(@RequestParam String keyword) {
        List<ProductDto> products = productService.searchProducts(keyword);
        return new ResultVO(ApiResponseCode.SUCCESS, "Products search completed successfully", products);
    }

    @GetMapping("/priceRange")
    public ResultVO getProductsByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return new ResultVO(ApiResponseCode.SUCCESS, "Products retrieved by price range successfully", products);
    }
}
