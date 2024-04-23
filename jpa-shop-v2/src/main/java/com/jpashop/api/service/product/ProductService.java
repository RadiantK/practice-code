package com.jpashop.api.service.product;

import com.jpashop.api.service.product.request.ProductCreateServiceRequest;
import com.jpashop.api.service.product.request.ProductEditServiceRequest;
import com.jpashop.api.service.product.request.ProductServiceRequest;
import com.jpashop.api.service.product.response.ProductCreateResponse;
import com.jpashop.api.service.product.response.ProductResponse;
import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findProducts(ProductServiceRequest request) {
        return productRepository.findAllBySellingStatusIn(request.getSellingStatuses()).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductCreateResponse saveProduct(ProductCreateServiceRequest request) {
        Product savedProduct = productRepository.save(request.toEntity());

        return ProductCreateResponse.of(savedProduct);
    }

    @Transactional
    public void editProduct(ProductEditServiceRequest request) {
        Product findProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품 id를 확인하세요."));

        findProduct.editProductInfo(
                request.getName(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getSellingStatus()
        );
    }
}
