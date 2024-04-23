package com.jpashop.api.controller.product;

import com.jpashop.api.ApiResponse;
import com.jpashop.api.controller.product.request.ProductCreateRequest;
import com.jpashop.api.controller.product.request.ProductRequest;
import com.jpashop.api.service.product.ProductService;
import com.jpashop.api.service.product.response.ProductCreateResponse;
import com.jpashop.api.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ApiResponse<List<ProductResponse>> findProducts(@Validated @RequestBody ProductRequest request) {
        return ApiResponse.of(productService.findProducts(request.toServiceRequest()));
    }

    @PostMapping("/products/new")
    public ApiResponse<ProductCreateResponse> createProduct(@Validated @RequestBody ProductCreateRequest request) {
        return ApiResponse.of(productService.saveProduct(request.toServiceRequest()));
    }
}
