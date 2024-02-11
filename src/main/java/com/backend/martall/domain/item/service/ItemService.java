package com.backend.martall.domain.item.service;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemResponseDto> searchItems(String itemName) {
        List<Item> items = itemRepository.findByItemName(itemName); // 인스턴스를 통해 메소드 호출
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}
