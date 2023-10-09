package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BookDto;
import jpabook.jpashop.dto.BookRequest;
import jpabook.jpashop.dto.ItemResponse;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("bookRequest", new BookRequest());

        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String create(BookRequest bookRequest) {
        itemService.saveItem(bookRequest.toBookDto());

        return "redirect:/items";
    }

    @GetMapping
    public String list(Model model) {
        List<ItemResponse> items = itemService.findItems();
        model.addAttribute("items", items);

        return "items/itemList";
    }
    
    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        ItemResponse itemResponse = itemService.findItem(itemId);
        BookRequest bookRequest = itemResponse.toBookRequest();
        model.addAttribute("bookRequest", bookRequest);

        return "items/updateItemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, BookRequest bookRequest) {
        itemService.updateItem(itemId, bookRequest.toBookDto());

        return "redirect:/items";
    }
}
