package com.jpashop.api.controller.product.request;

import com.jpashop.api.service.product.request.ProductEditServiceRequest;
import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class ProductEditRequest {

    @NotNull(message = "아이디는 필수입니다.")
    private Long id;

    @NotBlank(message = "상품명을 기입해주세요.")
    private String name;

    @Positive(message = "상품 가격은 양수입니다.")
    private int price;

    @Positive(message = "상품 수량은 양수입니다.")
    private int stockQuantity;

    @NotNull(message = "상품 판매 상태를 선택해주세요.")
    private SellingStatus sellingStatus;

    @Builder
    public ProductEditRequest(Long id, String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellingStatus = sellingStatus;
    }

    public ProductEditServiceRequest toServiceRequest() {
        return ProductEditServiceRequest.builder()
                .id(id)
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .sellingStatus(sellingStatus)
                .build();
    }
}
