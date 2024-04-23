package com.jpashop.api.service.product.response;

import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;

public class ProductResponse {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private SellingStatus sellingStatus;

    @Builder
    private ProductResponse(Long id, String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellingStatus = sellingStatus;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sellingStatus(product.getSellingStatus())
                .build();
    }
}
