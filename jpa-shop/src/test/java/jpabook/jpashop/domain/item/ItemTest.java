package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {

    @DisplayName("입력한 수량만큼 재고의 수량이 증가한다.")
    @Test
    void addStock() {
        // given
        int stockQuantity = 10;
        Item book = createBook("JPA BOOK", 10000, stockQuantity);

        // when
        int addQuantity = 3;
        book.addStock(addQuantity);

        // then
        assertThat(book.getStockQuantity()).isEqualTo(stockQuantity + addQuantity);
    }

    @DisplayName("입력한 수량만큼 재고의 수량에서 감소된다.")
    @Test
    void cancelStock() {
        // given
        int stockQuantity = 10;
        Item book = createBook("JPA BOOK", 10000, stockQuantity);

        // when
        int addQuantity = 3;
        book.removeStock(addQuantity);

        // then
        assertThat(book.getStockQuantity()).isEqualTo(stockQuantity - addQuantity);
    }

    @DisplayName("재고수량보다 많은 수량을 제거하면 예외가 발생한다.")
    @Test
    void overRemoveStock() {
        // given
        int stockQuantity = 10;
        Item book = createBook("JPA BOOK", 10000, stockQuantity);

        // when & then
        int addQuantity = 11;
        assertThatThrownBy(() -> book.removeStock(addQuantity))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("재고가 남아있지 않습니다.");
    }

    private Item createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author("kang")
                .build();
    }
}