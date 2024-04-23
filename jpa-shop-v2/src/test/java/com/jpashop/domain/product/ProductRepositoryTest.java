package com.jpashop.domain.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.jpashop.domain.product.SellingStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("모든 상품 목록을 조회한다.")
    @Test
    void findAll() {
        // given
        Product product1 = createProduct("노트", 5000, 5, SELLING);
        Product product2 = createProduct("볼펜", 1000, 3, HOLD);
        Product product3 = createProduct("형광펜", 2000, 4, STOP_SELLING);
        Product product4 = createProduct("JPA책", 20000, 10, SELLING);
        productRepository.saveAll(List.of(product1, product2, product3, product4));

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(4)
                .extracting("name", "price", "stockQuantity", "sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("노트", 5000, 5, SELLING),
                        Tuple.tuple("볼펜", 1000, 3, HOLD),
                        Tuple.tuple("형광펜", 2000, 4, STOP_SELLING),
                        Tuple.tuple("JPA책", 20000, 10, SELLING)
                );
    }

    @DisplayName("상품목록 중 원하는 판매 상태의 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("노트", 5000, 5, SELLING);
        Product product2 = createProduct("볼펜", 1000, 3, HOLD);
        Product product3 = createProduct("형광펜", 2000, 4, STOP_SELLING);
        Product product4 = createProduct("JPA책", 20000, 10, SELLING);
        productRepository.saveAll(List.of(product1, product2, product3, product4));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING));

        // then
        assertThat(products).hasSize(2)
                .extracting("name", "price", "stockQuantity", "sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("노트", 5000, 5, SELLING),
                        Tuple.tuple("JPA책", 20000, 10, SELLING)
                );
    }

    @DisplayName("상품을 하나 저장한다.")
    @Test
    void saveTest() {
        // given
        Product product = createProduct("노트", 5000, 5, SELLING);

        // when
        Product savedItem = productRepository.save(product);

        // then
        assertThat(savedItem).isNotNull();
        assertThat(savedItem).extracting("name", "price", "stockQuantity", "sellingStatus")
                .contains("노트", 5000, 5, SELLING);
    }

    private Product createProduct(String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .sellingStatus(sellingStatus)
                .build();
    }
}