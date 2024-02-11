package com.backend.martall.domain.item.service;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
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
        List<Item> items = itemRepository.findByItemName(itemName);
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }

    public ItemResponseDto getItemDetail(Long shopId, int itemId) {
        Item item = itemRepository.findById(shopId, itemId);
//                .map(ItemResponseDto::from)
//                .orElseThrow(() -> new BadRequestException(ResponseStatus.ITEM_DETAIL_FAIL));
        return ItemResponseDto.from(item);
    }

    public List<ItemResponseDto> newItems() {
        List<Item> items = itemRepository.findAllOrderByRegDateDesc();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}
