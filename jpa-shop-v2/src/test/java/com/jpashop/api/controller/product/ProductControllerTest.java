package com.jpashop.api.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpashop.api.controller.product.request.ProductCreateRequest;
import com.jpashop.api.controller.product.request.ProductRequest;
import com.jpashop.api.service.product.ProductService;
import com.jpashop.domain.product.SellingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("판매중인 상품의 전체 목록을 조회한다.")
    @Test
    void findProducts() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .sellingStatuses(List.of(SellingStatus.SELLING))
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
    }

    @DisplayName("판매중인 상품의 전체 목록을 조회할 때 판매 상태는 필수값이다.")
    @Test
    void findProductsWithoutSellingStatus() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @DisplayName("새로운 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("JPA책")
                .price(20000)
                .stockQuantity(10)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("새로운 상품을 등록할 때 상품명은 필수이며 빈값이나 공백일 수 없다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name(null)
                .price(20000)
                .stockQuantity(10)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품명을 기입해주세요."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("새로운 상품을 등록할 때 상품가격은 양수이다.")
    @Test
    void createProductWithoutPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("JPA책")
                .price(0)
                .stockQuantity(10)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 가격은 양수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("새로운 상품을 등록할 때 상품 수량은 양수이다.")
    @Test
    void createProductWithoutStockQuantity() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("JPA책")
                .price(20000)
                .stockQuantity(0)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 수량은 양수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andDo(MockMvcResultHandlers.print());
    }
}