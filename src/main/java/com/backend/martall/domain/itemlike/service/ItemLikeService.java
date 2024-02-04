package com.backend.martall.domain.itemlike.service;

import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemLikeService {

    private final ItemLikeRepository itemLikeRepository;

    public void addItemLike(int itemId) {

    }

    public void removeItemLike(int itemId) {

    }

}
