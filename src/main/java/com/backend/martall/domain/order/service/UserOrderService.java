package com.backend.martall.domain.order.service;

import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartItemRepository;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.order.dto.*;
import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.entity.OrderItem;
import com.backend.martall.domain.order.repository.OrderInfoRepository;
import com.backend.martall.domain.order.repository.OrderItemRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.martall.domain.order.entity.OrderState.ORDER_PREPARE;
import static com.backend.martall.global.exception.ResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final OrderInfoRepository orderInfoRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAsyncService orderAsyncService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MartRepository martRepository;


    // 주문 생성
    // 사용자 아이디의 장바구니 상품 -> 가게별로 분류 -> 같은 가게의 상품끼리 주문 생성
    @Transactional
    public void createOrder(OrderCreateRequestDto orderCreateRequestDto, Long userIdx) {



        User user = userRepository.findByUserIdx(userIdx).get();

        // 해당 아이디에 주문이 있는지 확인
        // 존재하면 주문을 추가할 수 없음
        if (orderInfoRepository.existsByUserAndOrderState(user, ORDER_PREPARE.getCode())) {
            log.info("주문이 이미 존재함, userIdx = {}", userIdx);
            throw new BadRequestException(ORDER_EXIST_OTHER_ORDER);
        }

        // 마트
        MartShop martShop = martRepository.findById(orderCreateRequestDto.getMartId()).get();

        // 주문 정보 생성
        OrderInfo orderInfo = OrderInfo.builder()
                .user(user) // --> 실제 아이디 추가로 변경
                .martShop(martShop)
                .orderState(ORDER_PREPARE.getCode())
                .build();

        log.info("주문생성, userIdx = {}", userIdx);
        orderInfoRepository.save(orderInfo);



        // 주문할 상품 리스트
        List<OrderCreateRequestDto.CartItem> cartItemList = orderCreateRequestDto.getCartItems();

        // 주문할 상품 목록이 비어있을 경우
        if(cartItemList.isEmpty()) {
            log.info("주문할 상품 목록이 비어있음, userIdx = {}", userIdx);
            throw new BadRequestException(ORDER_CART_EMPTY);
        }

        // 결제해야 하는 금액
        int realPayment = 0;

        // 주문 검증 및 저장
        for(OrderCreateRequestDto.CartItem orderCartItem:cartItemList){

            // 장바구니 상품 정보를 가져오기
            // 장바구에 상품이 해당 상품이 없으면 예외처리
            CartItem cartItem = cartItemRepository.findById(orderCartItem.getCartItemId())
                    .orElseThrow(() -> new BadRequestException(ORDER_CARTITEM_NOT_EXIST));


            // cartItem의 itemId 로 상품 가격 불러오기
            // 상품 가격 realPayment에 더하기
            Item item = cartItem.getItem();

            realPayment = realPayment + (item.getPrice() * cartItem.getCount());

            // 주문 상품 생성
            // itemId, count는 위의 cartItem에서 가져오기
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .orderInfo(orderInfo)
                    .count(cartItem.getCount())
                    .build();

            orderItemRepository.save(orderItem);

            // 주문으로 넘어간 장바구니 상품 삭제
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }
        log.info("주문의 상품목록 생성 완료, userIdx = {}", userIdx);

        // 결제 금액 확인
        if(realPayment != orderCreateRequestDto.getPayment()) {
            log.info("주문 금액과 결제 금액이 일치하지 않음, userIdx = {}, 주문금액 = {}, 결제금액 = {}", userIdx, realPayment, orderCreateRequestDto.getPayment());
            throw new BadRequestException(ORDER_PAYMENT_NOT_EQUAL);
        }

        orderAsyncService.changeOrderState(orderInfo);
    }

    public OrderInquiryResponseDto getOrder(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // id에 해당하는 orderinfo 불러오기 ( 준비중 P 인것만)
        Optional<OrderInfo> orderInfoOptional = orderInfoRepository.findByUserAndOrderState(user, ORDER_PREPARE.getCode());

        // 주문이 존재하지 않으면 예외처리
        OrderInfo orderInfo;

        try {
            orderInfo = orderInfoOptional.get();
        } catch (RuntimeException e) {
            log.info("준비중인 주문이 존재하지 않음, userIdx = {}", userIdx);
            return OrderInquiryResponseDto.builder()
                    .build();
        }

        // orderInfo로 해당 주문 상품 리스트 조회
        List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);

        MartShop martShop = orderInfo.getMartShop();

        // 주문 상품을 dto로 변환
        OrderInquiryResponseDto orderInquiryResponseDto = OrderInquiryResponseDto.builder()
                // 주문정보
                .order(OrderInquiryResponseDto.OrderInfo.builder()
                        .martId(martShop.getMartShopId())
                        .martName(martShop.getName())
                        .orderItemCount(orderItemList.size())
                        // 주문 총 가격
                        .payment(orderItemList.stream()
                                .mapToInt(orderItem -> {
                                    int price = orderItem.getItem().getPrice();
                                    int count = orderItem.getCount();
                                    return price*count;
                                })
                                .sum())
                        .build())
                // 주문상품
                .items(orderItemList.stream()
                        .map(orderItem -> {
                            Item item = orderItem.getItem();
                            return OrderInquiryResponseDto.OrderItem.builder()
                                    .orderItemId(orderItem.getOrderItemId())
                                    .itemId(item.getItemId())
                                    .itemImg(item.getProfilePhoto())
                                    .itemName(item.getItemName())
                                    .itemPrice(item.getPrice())
                                    .itemCategory(item.getCategoryId().getName())
                                    .orderItemCount(orderItem.getCount())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();

        log.info("주문 상품 조회 목록 생성, userIdx = {}, orderId = {}", userIdx, orderInfo.getOrderId());

        return orderInquiryResponseDto;
    }
}
