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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class ProductControllerTest {

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
    void testCreateAndGetProduct() throws Exception {
        CreateProductRequest createRequest = new CreateProductRequest();
        createRequest.setName("Test Product");
        createRequest.setDescription("A test product for testing");
        createRequest.setPrice(BigDecimal.valueOf(99.99));
        createRequest.setCategory("Test Category");

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        ProductDto createdProduct = objectMapper.convertValue(createResult.getResult(), ProductDto.class);
        Long productId = createdProduct.getId();

        assertEquals("Test Product", createdProduct.getName());
        assertEquals("A test product for testing", createdProduct.getDescription());
        assertEquals(BigDecimal.valueOf(99.99), createdProduct.getPrice());
        assertEquals("Test Category", createdProduct.getCategory());
        assertNotNull(createdProduct.getId());
        assertNotNull(createdProduct.getCreatedAt());
        assertNotNull(createdProduct.getUpdatedAt());

        String getResponse = mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Product retrieved successfully"))
                .andReturn().getResponse().getContentAsString();

        ResultVO getResult = objectMapper.readValue(getResponse, ResultVO.class);
        ProductDto retrievedProduct = objectMapper.convertValue(getResult.getResult(), ProductDto.class);

        assertEquals(productId, retrievedProduct.getId());
        assertEquals("Test Product", retrievedProduct.getName());
        assertEquals("A test product for testing", retrievedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(99.99), retrievedProduct.getPrice());
        assertEquals("Test Category", retrievedProduct.getCategory());
        assertEquals(createdProduct.getCreatedAt(), retrievedProduct.getCreatedAt());
        assertEquals(createdProduct.getUpdatedAt(), retrievedProduct.getUpdatedAt());
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Products retrieved successfully"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        CreateProductRequest createRequest = new CreateProductRequest();
        createRequest.setName("Update Test Product");
        createRequest.setDescription("Product for update testing");
        createRequest.setPrice(BigDecimal.valueOf(49.99));
        createRequest.setCategory("Test Category");

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        ProductDto createdProduct = objectMapper.convertValue(createResult.getResult(), ProductDto.class);
        Long productId = createdProduct.getId();

        assertEquals("Update Test Product", createdProduct.getName());
        assertEquals(BigDecimal.valueOf(49.99), createdProduct.getPrice());

        CreateProductRequest updateRequest = new CreateProductRequest();
        updateRequest.setName("Updated Product Name");
        updateRequest.setDescription("Updated description");
        updateRequest.setPrice(BigDecimal.valueOf(59.99));
        updateRequest.setCategory("Updated Category");

        String updateJson = objectMapper.writeValueAsString(updateRequest);

        // Adding a small delay to ensure timestamps are different
        Thread.sleep(1);

        String updateResponse = mockMvc.perform(put("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Product updated successfully"))
                .andReturn().getResponse().getContentAsString();

        ResultVO updateResult = objectMapper.readValue(updateResponse, ResultVO.class);
        ProductDto updatedProduct = objectMapper.convertValue(updateResult.getResult(), ProductDto.class);

        assertEquals(productId, updatedProduct.getId());
        assertEquals("Updated Product Name", updatedProduct.getName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(59.99), updatedProduct.getPrice());
        assertEquals("Updated Category", updatedProduct.getCategory());
        assertEquals(createdProduct.getCreatedAt(), updatedProduct.getCreatedAt());
    }

    @Test
    void testDeleteProduct() throws Exception {
        CreateProductRequest createRequest = new CreateProductRequest();
        createRequest.setName("Delete Test Product");
        createRequest.setDescription("Product for delete testing");
        createRequest.setPrice(BigDecimal.valueOf(29.99));
        createRequest.setCategory("Test Category");

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ResultVO createResult = objectMapper.readValue(createResponse, ResultVO.class);
        ProductDto createdProduct = objectMapper.convertValue(createResult.getResult(), ProductDto.class);
        Long productId = createdProduct.getId();

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        mockMvc.perform(get("/api/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Products retrieved by category successfully"));
    }

    @Test
    void testSearchProducts() throws Exception {
        mockMvc.perform(get("/api/products/search")
                .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Products search completed successfully"));
    }

    @Test
    void testGetProductsByPriceRange() throws Exception {
        mockMvc.perform(get("/api/products/priceRange")
                .param("minPrice", "10.00")
                .param("maxPrice", "100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Products retrieved by price range successfully"));
    }
}
