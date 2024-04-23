package com.jpashop.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jpashop.domain.product.SellingStatus.SELLING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품의 재고 수량이 제공된 수량보다 적은지 확인한다.")
    @Test
    void isEnoughStockQuantity() {
        // given
        Product product = createProduct("JPA책", 5);
        int quantity = 3;

        // when
        boolean result = product.isEnoughStockQuantity(quantity);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("상품의 재고 수량이 제공된 수량보다 적은지 확인한다.")
    @Test
    void isEnoughStockQuantity2() {
        // given
        Product product = createProduct("JPA책", 5);
        int quantity = 6;

        // when
        boolean result = product.isEnoughStockQuantity(quantity);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품의 재고 수량에서 제공된 수량만큼 감소한다.")
    @Test
    void deductQuantity() {
        // given
        Product product = createProduct("JPA책", 5);
        int quantity = 3;

        // when
        product.deductQuantity(quantity);

        // then
        assertThat(product.getStockQuantity()).isEqualTo(2);
    }

    @DisplayName("상품의 재고 수량보다 제공된 수량이 더 크면 예외가 발생한다.")
    @Test
    void deductQuantity2() {
        // given
        Product product = createProduct("JPA책", 5);
        int quantity = 6;

        // when & then
        assertThatThrownBy(() -> product.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고 수량이 부족합니다.");
    }

    private Product createProduct(String name, int stockQuantity) {
        return Product.builder()
                .name(name)
                .price(1000)
                .stockQuantity(stockQuantity)
                .sellingStatus(SELLING)
                .build();
    }

}