package com.backend.martall.domain.cart.service;

import com.backend.martall.domain.cart.dto.CartRequestDto;
import com.backend.martall.domain.cart.dto.CartRequestDtoList;
import com.backend.martall.domain.cart.dto.CartResponseDto;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;


    // 장바구니 상품 전체 출력
    // 추후 사용자 아이디 장바구니를 불러오는 것으로 수정
    @Transactional
    public List<CartResponseDto> getAllCartItem() {
        List<CartItem> cartItemList = cartRepository.findAll();
        List<CartResponseDto> cartResponseDtoList = cartItemList.stream()
                .map(CartResponseDto::of)
                .collect(Collectors.toList());

        log.info("장바구니 전체 조회, user id = {}", 1);

        return cartResponseDtoList;
    }

    // 장바구니 상품 추가
    // 추후 사용자 아이디 장바구니에를 추가하는 것으로 수정
    @Transactional
    public void addCartItem(CartRequestDto cartRequestDto) {
        if(dupCheck(cartRequestDto)){
            CartItem newCartItem = CartItem.builder()
                    .itemId(cartRequestDto.getItemId())
                    .number(cartRequestDto.getNumber())
                    //.userIdx()  -> 유저 아이디 넣는 부분
                    .build();

            log.info("장바구니 상품 추가, user id = {}", 1);

            cartRepository.save(newCartItem);
        } else {
            throw new RuntimeException();
        }

    }


    // 중복 상품 확인
    public boolean dupCheck(CartRequestDto cartRequestDto){
        if(cartRepository.existsByItemId(cartRequestDto.getItemId())) {
            return false;
        } else {
            return true;
        }
    }

    // 장바구니 상품 수정
    // 추후 사용자 아이디 장바구니의 상품을 수정하는 것으로 수정
    @Transactional
    public void updateCartItem(CartRequestDto cartRequestDto) {
        Optional<CartItem> cartItemOptional = cartRepository.findById(cartRequestDto.getCartItemId());
        if(cartItemOptional.isPresent()) {
            CartItem updateCartItem = cartItemOptional.get();
            updateCartItem.updateCartItem(cartRequestDto);

            log.info("장바구니 상품 수정, user id = {}, cart_item_id = {}", 1, updateCartItem.getCartItemId());

        } else {
            throw new RuntimeException();
        }
    }

    // 장바구니 상품 삭제
    // 추후 사용자 아이디 장바구니의 상품을 삭제하는 것으로 수정
    @Transactional
    public void deleteCartItem(CartRequestDtoList cartRequestDtoList) {

        for(CartRequestDto cartRequestDto:cartRequestDtoList.getCartItemList()) {
            Optional<CartItem> cartItemOptional = cartRepository.findById(cartRequestDto.getCartItemId());
            if(cartItemOptional.isPresent()) {
                CartItem deleteCartItem = cartItemOptional.get();
                cartRepository.delete(deleteCartItem);

                log.info("장바구니 상품 삭제, user id = {}, cart_item_id = {}", 1, deleteCartItem.getCartItemId());
            } else {
                throw new RuntimeException();
            }
        }

    }
}
