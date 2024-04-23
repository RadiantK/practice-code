package com.jpashop.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SellingStatusTest {

    @DisplayName("상품이 판매중인 상품인지 확인한다.")
    @Test
    void isSellingTest() {
        // given
        SellingStatus status = SellingStatus.SELLING;

        // when
        boolean result = SellingStatus.isSelling(status);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("상품이 판매중인 상품인지 확인한다.")
    @Test
    void isSellingTest2() {
        // given
        SellingStatus status = SellingStatus.HOLD;

        // when
        boolean result = SellingStatus.isSelling(status);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품이 판매중인 상품인지 확인한다.")
    @Test
    void isSellingTest3() {
        // given
        SellingStatus status = SellingStatus.STOP_SELLING;

        // when
        boolean result = SellingStatus.isSelling(status);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품이 판매중인 상품인지 확인한다.")
    @MethodSource // 파라미터 정의 메서드가 테스트 메서드와 메서드 명이 같으면 생략가능
    @ParameterizedTest(name = "[{index}] \"{0}\" => \"{1}\"")
    void isSellingParameterizedTest(SellingStatus sellingStatus, boolean expected) {
        // when
        boolean result = SellingStatus.isSelling(sellingStatus);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> isSellingParameterizedTest() {
        return Stream.of(
                Arguments.of(SellingStatus.SELLING, true),
                Arguments.of(SellingStatus.HOLD, false),
                Arguments.of(SellingStatus.STOP_SELLING, false)
        );
    }
}