package com.backend.martall.domain.cart.service;

import com.backend.martall.domain.cart.dto.*;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartItemRepository;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.entity.MartShop;
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


    // 장바구니 조회
    @Transactional
    public CartInquiryResponseDto inquiryCart(Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        List<CartItem> cartItemList = cartItemRepository.findByUser(user);

        // 장바구니에 상품이 없으면 빈 장바구니 반환
        if (cartItemList.isEmpty()) {
            return CartInquiryResponseDto.builder()
                    .build();
        }


        // 첫번째 장바구니 상품에서 마트 불러오기
        MartShop martShop = cartItemList.get(0).getItem().getMartShop();

        // dto 생성
        CartInquiryResponseDto cartInquiryResponseDto = CartInquiryResponseDto.builder()
                // 마트 정보
                .mart(CartInquiryResponseDto.Mart.builder()
                        .martId(martShop.getMartShopId())
                        .martName(martShop.getName())
                        .martCategory(martShop.getMartCategories().stream()
                                .map(martCategory -> martCategory.getCategoryName())
                                .collect(Collectors.toList()))
                        .bookmarkCount(martShop.getMartBookmarks().size())
                        .likeCount(itemLikeService.countItemLikeByMart(martShop))
                        .build())
                // 상품 정보
                .items(cartItemList.stream()
                        .map(cartItem -> {
                            Item item = cartItem.getItem();
                            return CartInquiryResponseDto.CartItem.builder()
                                    .cartItemId(cartItem.getCartItemId())
                                    .itemId(item.getItemId())
                                    .itemImg(item.getProfilePhoto())
                                    .itemName(item.getItemName())
                                    .itemPrice(item.getPrice())
                                    .itemCategory(item.getCategoryId().getName())
                                    .cartItemCount(cartItem.getCount())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();


        log.info("장바구니 상품 조회, userIdx = {}", userIdx);
        // 마트 정보 추가도 나중에 추가
        // checkCartMart로 마트정보 얻기

        return cartInquiryResponseDto;
    }


    // 장바구니 상품 추가
    // 추후 사용자 아이디 장바구니에 추가하는 것으로 수정
    @Transactional
    public void addCartItem(CartItemAddRequestDto cartItemAddRequestDto, Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        List<CartItem> cartItemList = cartItemRepository.findByUser(user);

        // item 이 존재하지 않으면 예외처리
        Item item = itemRepository.findById(cartItemAddRequestDto.getItemId())
                .orElseThrow(() -> new BadRequestException(CART_MART_NOT_EQUAL));


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
                .count(cartItemAddRequestDto.getCartItemCount())
                .user(user)  //-> 유저 아이디 넣는 부분
                .build();

        log.info("장바구니 상품 추가, userIdx = {}, itemId = {}", userIdx, cartItemAddRequestDto.getItemId());
        cartItemRepository.save(newCartItem);


    }


    // 장바구니 상품 수정
    // 추후 사용자 아이디 장바구니의 상품을 수정하는 것으로 수정
    @Transactional
    public void updateCartItem(CartItemUpdateRequestDto cartItemUpdateRequestDto, Long userIdx) {

        // cartItemId에 해당하는 상품이 장바구니에 없으면 에러
        CartItem updateCartItem = cartItemRepository.findById(cartItemUpdateRequestDto.getCartItemId())
                .orElseThrow(() -> new BadRequestException(CART_ITEM_NOT_EXIST));


        // 장바구니 상품의 회원과 수정을 요청한 회원이 일치하지 않으면 예외처리
        if(!updateCartItem.getUser().getUserIdx().equals(userIdx)) {
            throw new BadRequestException(CART_USER_NOT_EQUAL);
        }

        updateCartItem.updateCartItem(cartItemUpdateRequestDto);

        log.info("장바구니 상품 수정, user id = {}, cart_item_id = {}", userIdx, cartItemUpdateRequestDto.getCartItemId());
    }

    // 장바구니 상품 삭제
    // 추후 사용자 아이디 장바구니의 상품을 삭제하는 것으로 수정
    @Transactional
    public void deleteCartItem(CartItemDeleteRequestDto cartItemDeleteRequestDto, Long userIdx) {

        for (CartItemDeleteRequestDto.DeleteItem deleteItem : cartItemDeleteRequestDto.getCartItems()) {

            // 장바구니에 없는 상품이면 예외처리
            CartItem deleteCartItem = cartItemRepository.findById(deleteItem.getCartItemId())
                    .orElseThrow(() -> new BadRequestException(CART_ITEM_NOT_EXIST));

            // 장바구니 상품의 회원과 수정을 요청한 회원이 일치하지 않으면 예외처리
            if(!deleteCartItem.getUser().getUserIdx().equals(userIdx)) {
                throw new BadRequestException(CART_USER_NOT_EQUAL);
            }

            log.info("장바구니 상품 삭제, user id = {}, cart_item_id = {}", userIdx, deleteItem.getCartItemId());
            cartItemRepository.delete(deleteCartItem);

        }
    }

}
