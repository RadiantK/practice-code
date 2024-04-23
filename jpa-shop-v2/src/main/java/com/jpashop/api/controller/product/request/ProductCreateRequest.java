package com.jpashop.api.controller.product.request;

import com.jpashop.api.service.product.request.ProductCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "상품명을 기입해주세요.")
    private String name;

    @Positive(message = "상품 가격은 양수입니다.")
    private int price;

    @Positive(message = "상품 수량은 양수입니다.")
    private int stockQuantity;

    @Builder
    public ProductCreateRequest(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}
