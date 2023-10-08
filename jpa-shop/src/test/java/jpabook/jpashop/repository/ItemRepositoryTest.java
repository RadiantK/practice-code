package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @DisplayName("상품 저장")
    @Test
    void save() {
        // given
        Item item = createBook("JPA BOOK", 10000, 4);

        // when
        Long savedId = itemRepository.save(item);

        // then
        assertThat(savedId).isEqualTo(item.getId());
    }

    @DisplayName("상품 전체 조회")
    @Test
    void findAll() {
        // given
        Item item1 = createBook("JPA BOOK", 10000, 4);
        Item item2 = createBook("MYSQL BOOK", 10000, 2);

        itemRepository.save(item1);
        itemRepository.save(item2);

        // when
        List<Item> findItems = itemRepository.findAll();

        // then
        assertThat(findItems).hasSize(2)
                .extracting("name", "price", "stockQuantity")
                .containsExactlyInAnyOrder(
                        tuple("JPA BOOK", 10000, 4),
                        tuple("MYSQL BOOK", 10000, 2)
                );
    }

    @DisplayName("상품 단건 조회")
    @Test
    void findById() {
        // given
        Item item = createBook("JPA BOOK", 10000, 4);

        Long savedId = itemRepository.save(item);

        // when
        Item findItem = itemRepository.findById(savedId);

        // then
        assertThat(findItem).isNotNull();
        assertThat(findItem.getId()).isEqualTo(savedId);
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