package com.backend.martall.domain.cart.service;

import com.backend.martall.domain.cart.dto.*;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartItemRepository;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemLikeService itemLikeService;
    private final ItemRepository itemRepository;

    /*
        likecount 로직 추가되면 추가

     */

    @Transactional
    public CartInquiryResponse inquiryCart(Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        List<CartItem> cartItemList = cartItemRepository.findByUser(user);

        // 장바구니에 상품이 없으면 에러
        if (cartItemList.isEmpty()) {
            log.info("장바구니에 상품이 존재하지 않음, userIdx = {}", userIdx);
            throw new BadRequestException(CART_ITEM_NOT_EXIST);
        }

        // 장바구니에서 상품 아이디
        Item item = cartItemList.get(0).getItem();
        // 상품 아이디에서 마트 아이디
        MartShop martShop = item.getMartShop();

        List<CartMartShopCategoryResponse> cartMartShopCategoryResponseList = martShop.getMartCategories().stream()
                .map(CartMartShopCategoryResponse::of)
                .collect(Collectors.toList());

        CartMartShopResponse cartMartShopResponse = CartMartShopResponse.builder()
                .martShopId(martShop.getMartShopId())
                .martName(martShop.getName())
                .martCategory(cartMartShopCategoryResponseList)
                .bookmarkCount(martShop.getMartBookmarks().size())
                .likeCount(itemLikeService.countItemLikeByMart(martShop))
                .build();

        List<CartItemResponse> cartResponseDtoList = cartItemList.stream()
                .map(cartItem -> {
                    CartItemResponse cartItemResponse = CartItemResponse.of(cartItem);
                    cartItemResponse.setItemName(cartItem.getItem().getItemName());
                    cartItemResponse.setCategoryName(cartItem.getItem().getCategoryId().getName());
                    cartItemResponse.setPrice(cartItem.getItem().getPrice());
                    cartItemResponse.setPicName(cartItem.getItem().getProfilePhoto());
                    return cartItemResponse;
                })
                .collect(Collectors.toList());

        log.info("장바구니 상품 조회, userIdx = {}", userIdx);
        // 마트 정보 추가도 나중에 추가
        // checkCartMart로 마트정보 얻기

        return CartInquiryResponse.builder()
                .mart(cartMartShopResponse)
                .cartItemResponseList(cartResponseDtoList)
                .build();
    }


    // 장바구니 상품 추가
    // 추후 사용자 아이디 장바구니에 추가하는 것으로 수정
    @Transactional
    public void addCartItem(CartItemRequest cartItemRequest, Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        List<CartItem> cartItemList = cartItemRepository.findByUser(user);

        Optional<Item> optionalItem = itemRepository.findById(cartItemRequest.getItemId());

        Item item;
        // item 이 존재하지 않으면 예외처리
        try {
            item = optionalItem.get();
        } catch (RuntimeException e) {
            throw new BadRequestException(CART_MART_NOT_EQUAL);
        }

        // 장바구니가 비어있지 않으면
        if(!cartItemList.isEmpty()) {
            MartShop cartMartShop = cartItemList.get(0).getItem().getMartShop();

            // 장바구니 상품과 추가하려는 상품의 마트가 같지 않으면
            if (!item.getMartShop().equals(cartMartShop)) {
                throw new BadRequestException(CART_MART_NOT_EQUAL);
            }
        }


        // 엔티티 생성
        CartItem newCartItem = CartItem.builder()
                .item(item)
                .count(cartItemRequest.getCount())
                .user(user)  //-> 유저 아이디 넣는 부분
                .build();

        log.info("장바구니 상품 추가, userIdx = {}, itemId = {}", userIdx, cartItemRequest.getItemId());
        cartItemRepository.save(newCartItem);


    }


    // 장바구니 상품 수정
    // 추후 사용자 아이디 장바구니의 상품을 수정하는 것으로 수정
    @Transactional
    public void updateCartItem(CartItemRequest cartRequestDto, Long userIdx) {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartRequestDto.getCartItemId());
        CartItem updateCartItem;

        // cartItemId에 해당하는 상품이 장바구니에 없으면 에러
        try {

            updateCartItem = cartItemOptional.get();

        } catch (RuntimeException e) {

            log.info("수정하려는 장바구니 상품이 없음 , userIdx = {}, cart_item_id = {}", userIdx, cartRequestDto.getCartItemId());
            throw new BadRequestException(CART_ITEM_NOT_EXIST);

        }

        if(!updateCartItem.getUser().getUserIdx().equals(userIdx)) {
            throw new BadRequestException(CART_USER_NOT_EQUAL);
        }

        updateCartItem.updateCartItem(cartRequestDto);

        log.info("장바구니 상품 수정, user id = {}, cart_item_id = {}", userIdx, cartRequestDto.getCartItemId());
    }

    // 장바구니 상품 삭제
    // 추후 사용자 아이디 장바구니의 상품을 삭제하는 것으로 수정
    @Transactional
    public void deleteCartItem(CartItemRequestList cartRequestDtoList, Long userIdx) {

        for (CartItemRequest cartRequestDto : cartRequestDtoList.getCartItemList()) {
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartRequestDto.getCartItemId());

            CartItem deleteCartItem;

            try {

                deleteCartItem = cartItemOptional.get();

            } catch (RuntimeException e) {

                log.info("삭제하려는 장바구니 상품이 없음, user id = {}, cart_item_id = {}", userIdx, cartRequestDto.getCartItemId());
                throw new BadRequestException(CART_ITEM_NOT_EXIST);

            }

            if(!deleteCartItem.getUser().getUserIdx().equals(userIdx)) {
                throw new BadRequestException(CART_USER_NOT_EQUAL);
            }

            log.info("장바구니 상품 삭제, user id = {}, cart_item_id = {}", userIdx, cartRequestDto.getCartItemId());
            cartItemRepository.delete(deleteCartItem);

        }
    }

}
