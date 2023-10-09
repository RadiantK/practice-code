package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BookDto;
import jpabook.jpashop.dto.ItemResponse;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(BookDto bookDto) {
        Long savedId = itemRepository.save(bookDto.toEntity());

        return savedId;
    }

    @Transactional
    public void updateItem(Long itemId, BookDto bookDto) {
        Item findItem = itemRepository.findById(itemId);
        findItem.editItem(bookDto.getName(), bookDto.getPrice(), bookDto.getStockQuantity());
    }

    public List<ItemResponse> findItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::of)
                .collect(Collectors.toList());
    }

    public ItemResponse findItem(Long itemId) {
        return ItemResponse.of(itemRepository.findById(itemId));
    }
}
