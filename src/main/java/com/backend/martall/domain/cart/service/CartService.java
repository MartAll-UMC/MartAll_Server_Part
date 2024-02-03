package com.backend.martall.domain.cart.service;

import com.backend.martall.domain.cart.dto.CartInquiryResponse;
import com.backend.martall.domain.cart.dto.CartItemRequest;
import com.backend.martall.domain.cart.dto.CartItemRequestList;
import com.backend.martall.domain.cart.dto.CartItemResponse;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartItemRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.CART_ITEM_NOT_EXIST;
import static com.backend.martall.global.exception.ResponseStatus.CART_USER_NOT_EQUAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartInquiryResponse inquiryCart() {

        // 마트 정보 추가도 나중에 추가
        // checkCartMart로 마트정보 얻기

        return CartInquiryResponse.builder()
                .cartItemResponseList(getCartItem())
                .build();
    }

    // 장바구니 상품 리스트
    @Transactional
    public List<CartItemResponse> getCartItem() {

        // 나중에 findByUserIdx 로 교체
        List<CartItem> cartItemList = cartItemRepository.findByUserIdx(1L);

        // 장바구니에 상품이 없으면 에러
        if (cartItemList.isEmpty()) {
            throw new BadRequestException(CART_ITEM_NOT_EXIST);
        }

        List<CartItemResponse> cartResponseDtoList = cartItemList.stream()
                .map(cartItem -> {
                    CartItemResponse cartItemResponse = CartItemResponse.of(cartItem);
//                    cartItemResponse.setItemName();
//                    cartItemResponse.setCategoryName();
//                    cartItemResponse.setPrice();
//                    cartItemResponse.setPicName();
                    return cartItemResponse;
                })
                .collect(Collectors.toList());

        log.info("장바구니 전체 조회, user id = {}", 1);

        return cartResponseDtoList;
    }


    // 장바구니 상품 추가
    // 추후 사용자 아이디 장바구니에를 추가하는 것으로 수정
    @Transactional
    public void addCartItem(CartItemRequest cartItemRequest) {

        // 장바구니의 상품과 같은 마트의 상품인지 확인하고 에러
        // 나중에 findByUserIdx 로 교체
        List<CartItem> cartItemList = cartItemRepository.findByUserIdx(1L);


        // 리스트 1번째 상품 id로 마트샵 아이디 get
        // dto의 상품 id로 마트샵 아이디 get
        // ----> checkCartMart로 진행
        // 비교 후 진행

        // 엔티티 생성
        CartItem newCartItem = CartItem.builder()
                .itemId(cartItemRequest.getItemId())
                .count(cartItemRequest.getCount())
                .userIdx(1L)  //-> 유저 아이디 넣는 부분
                .build();

        log.info("장바구니 상품 추가 : Success , user id = {}", 1);
        cartItemRepository.save(newCartItem);


    }


    //사용자 장바구니 상품의 마트 id 불러오기
    @Transactional
    public void checkCartMart() {
        // 상품 정보 불러오기

        // 상품이 속해있는 마트ID 확인

        // 사용자 idx에 해당하는 장바구니 상품 불러오고
    }

    // 장바구니 상품 수정
    // 추후 사용자 아이디 장바구니의 상품을 수정하는 것으로 수정
    @Transactional
    public void updateCartItem(CartItemRequest cartRequestDto) {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartRequestDto.getCartItemId());
        CartItem updateCartItem;

        // cartItemId에 해당하는 상품이 장바구니에 없으면 에러
        try {

            updateCartItem = cartItemOptional.get();

        } catch (RuntimeException e) {

            log.info("장바구니 상품 수정 : Fail 상품 없음 , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
            throw new BadRequestException(CART_ITEM_NOT_EXIST);

        }

        if(checkCartItemUserIdx(1L, updateCartItem)) {
            throw new BadRequestException(CART_USER_NOT_EQUAL);
        }

        updateCartItem.updateCartItem(cartRequestDto);

        log.info("장바구니 상품 수정 : Success , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
    }

    // 장바구니 상품 삭제
    // 추후 사용자 아이디 장바구니의 상품을 삭제하는 것으로 수정
    @Transactional
    public void deleteCartItem(CartItemRequestList cartRequestDtoList) {

        for (CartItemRequest cartRequestDto : cartRequestDtoList.getCartItemList()) {
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartRequestDto.getCartItemId());

            CartItem deleteCartItem;

            try {

                deleteCartItem = cartItemOptional.get();

            } catch (RuntimeException e) {

                log.info("장바구니 상품 삭제 : Fail 상품 없음 , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
                throw new BadRequestException(CART_ITEM_NOT_EXIST);

            }

            if(checkCartItemUserIdx(1L, deleteCartItem)) {
                throw new BadRequestException(CART_USER_NOT_EQUAL);
            }

            log.info("장바구니 상품 삭제 : Success , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
            cartItemRepository.delete(deleteCartItem);

        }
    }

    public boolean checkCartItemUserIdx(Long userIdx, CartItem cartItem) {
        if (userIdx.equals(cartItem.getUserIdx())) {
            return true;
        } else {
            return false;
        }
    }
}
