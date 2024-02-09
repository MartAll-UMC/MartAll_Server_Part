package com.backend.martall.domain.item.service;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ItemService {

    public static List<ItemResponseDto> searchItems(String itemName) {
        List<Item> items = ItemRepository.findByItemName(itemName);
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}
