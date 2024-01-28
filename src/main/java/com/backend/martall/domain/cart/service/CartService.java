package com.backend.martall.domain.cart.service;

import com.backend.martall.domain.cart.dto.CartItemRequest;
import com.backend.martall.domain.cart.dto.CartItemRequestList;
import com.backend.martall.domain.cart.dto.CartItemResponse;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.CART_ITEM_DUP;
import static com.backend.martall.global.exception.ResponseStatus.CART_ITEM_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;


    // 장바구니 상품 전체 출력
    // 추후 사용자 아이디 장바구니를 불러오는 것으로 수정
    @Transactional
    public List<CartItemResponse> getAllCartItem() {
        List<CartItem> cartItemList = cartRepository.findAll();
        List<CartItemResponse> cartResponseDtoList = cartItemList.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());

        log.info("장바구니 전체 조회, user id = {}", 1);

        return cartResponseDtoList;
    }

    // 장바구니 상품 추가
    // 추후 사용자 아이디 장바구니에를 추가하는 것으로 수정
    @Transactional
    public void addCartItem(CartItemRequest cartRequestDto) {

        // 중복된 상품 있으면 에러
        if(!dupCheck(cartRequestDto)) {
            log.info("장바구니 상품 추가 : Fail 중복 상품 , user id = {}", 1);
            throw new BadRequestException(CART_ITEM_DUP);
        }

        // 상품이 존재하는지 확인하고 없으면 에러

        CartItem newCartItem = CartItem.builder()
                .itemId(cartRequestDto.getItemId())
                .count(cartRequestDto.getCount())
                //.userIdx()  -> 유저 아이디 넣는 부분
                .build();

        log.info("장바구니 상품 추가 : Success , user id = {}", 1);
        cartRepository.save(newCartItem);


    }


    // 중복 상품 확인
    public boolean dupCheck(CartItemRequest cartRequestDto){
        if(cartRepository.existsByItemId(cartRequestDto.getItemId())) {
            return false;
        } else {
            return true;
        }
    }

    // 장바구니 상품 수정
    // 추후 사용자 아이디 장바구니의 상품을 수정하는 것으로 수정
    @Transactional
    public void updateCartItem(CartItemRequest cartRequestDto) {

        Optional<CartItem> cartItemOptional = cartRepository.findById(cartRequestDto.getCartItemId());
        CartItem updateCartItem;

        // cartItemId에 해당하는 상품이 장바구니에 없으면 에러
        try {

             updateCartItem = cartItemOptional.get();

        } catch (RuntimeException e) {

            log.info("장바구니 상품 수정 : Fail 상품 없음 , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
            throw new BadRequestException(CART_ITEM_NOT_EXIST);

        }

        updateCartItem.updateCartItem(cartRequestDto);

        log.info("장바구니 상품 수정 : Success , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
    }

    // 장바구니 상품 삭제
    // 추후 사용자 아이디 장바구니의 상품을 삭제하는 것으로 수정
    @Transactional
    public void deleteCartItem(CartItemRequestList cartRequestDtoList) {

        for(CartItemRequest cartRequestDto:cartRequestDtoList.getCartItemList()) {
            Optional<CartItem> cartItemOptional = cartRepository.findById(cartRequestDto.getCartItemId());
            CartItem deleteCartItem;

            try {

                deleteCartItem = cartItemOptional.get();

            } catch (RuntimeException e) {

                log.info("장바구니 상품 삭제 : Fail 상품 없음 , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
                throw new BadRequestException(CART_ITEM_NOT_EXIST);

            }

            log.info("장바구니 상품 삭제 : Success , user id = {}, cart_item_id = {}", 1, cartRequestDto.getCartItemId());
            cartRepository.delete(deleteCartItem);

        }

    }
}
