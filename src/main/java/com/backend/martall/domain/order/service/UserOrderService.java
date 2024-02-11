package com.backend.martall.domain.order.service;

import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.repository.CartItemRepository;
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
    public void createOrder(OrderCreateRequest orderCreateRequest, Long userIdx) {

        // 결제해야 하는 금액
        int realPayment = 0;

        User user = userRepository.findByUserIdx(userIdx).get();

        // 해당 아이디에 주문이 있는지 확인
        // 존재하면 주문을 추가할 수 없음
        if(orderInfoRepository.existsByUserAndOrderState(user, ORDER_PREPARE.getCode())) {
            log.info("주문이 이미 존재함, userIdx = {}", userIdx);
            throw new BadRequestException(ORDER_EXIST_OTHER_ORDER);
        }

        MartShop martShop = martRepository.findById(orderCreateRequest.getMartShopId()).get();

        // 주문 정보 생성
        OrderInfo orderInfo = OrderInfo.builder()
                .user(user) // --> 실제 아이디 추가로 변경
                .martShop(martShop)
                .orderState(ORDER_PREPARE.getCode())
                .build();

        log.info("주문생성, userIdx = {}", userIdx);
        orderInfoRepository.save(orderInfo);



        // 주문할 상품 리스트
        List<OrderItemCreateRequest> cartItemList = orderCreateRequest.getCartItemList();

        // 주문할 상품 목록이 비어있을 경우
        if(cartItemList.isEmpty()) {
            log.info("주문할 상품 목록이 비어있음, userIdx = {}", userIdx);
            throw new BadRequestException(ORDER_CART_EMPTY);
        }

        for(OrderItemCreateRequest createRequest:cartItemList){

            // 장바구니 상품 정보를 가져오기
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(createRequest.getCartItemId());
            CartItem cartItem;
            try {
                cartItem = cartItemOptional.get();
            } catch (RuntimeException e) {
                log.info("주문할 상품 목록이 존재하지 않음, user id = {}", userIdx);
                throw new BadRequestException(ORDER_CARTITEM_NOT_EXIST);
            }

            // cartItem의 itemId 로 상품 가격 불러오기
            // 상품 가격 realPayment에 더하기
            realPayment = realPayment + 1000;

            // itemId, count는 위의 cartItem에서 가져오기
            OrderItem orderItem = OrderItem.builder()
                    .itemId(cartItem.getItemId())
                    .orderInfo(orderInfo)
                    .count(cartItem.getCount())
                    .build();

            orderItemRepository.save(orderItem);
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }
        log.info("주문의 상품목록 생성 완료, userIdx = {}", userIdx);

        // 결제 금액 확인
        if(realPayment != orderCreateRequest.getTotalPayment()) {
            log.info("주문 금액과 결제 금액이 일치하지 않음, userIdx = {}, 주문금액 = {}, 결제금액 = {}", userIdx, realPayment, orderCreateRequest.getTotalPayment());
            throw new BadRequestException(ORDER_PAYMENT_NOT_EQUAL);
        }

        orderAsyncService.changeOrderState(orderInfo);
//        ---> mart별로 아이템 분류
//        ************************* 봉인 ****************************
//        // orderItemAddRequest -> cartItemId -> cartItem의 itemId
//        // 금액 계산
//        // -> item의 price
//        // 마트별로 주문 나누기
//        // -> item의 martShopId
//
//        Map<String, List<OrderItem>> orderItemMap = new HashMap<>();
//        for(OrderItemCreateRequest cartItem:cartItemList) {
//            // cart repository에서 cart를 불러와 itemId 불러오기
//            // itemId로 item repository에서 item 불러오기
//            OrderItem orderItem = OrderItem.builder()
//                    .itemId(cartItem.getItemId())
//                    .count(cartItem.getCount())
//                    .build();
//            // item의 금액 payment에 더하기
//            realPayment += 1000;
//            // martShopId 별로 상품 리스트에 정리
//            orderItemMap.putIfAbsent(cartItem.getMartShopId(), new ArrayList<>());
//            orderItemMap.get(cartItem.getMartShopId()).add(orderItem);
//        }
//      ************************************************************

//        ---> 마트별로 주문 생성
//        ************************* 봉인 ****************************
//        // 리스트에 정리한 상품으로 주문 생성
//        for (Map.Entry<String, List<OrderItem>> entry:orderItemMap.entrySet()) {
//            String martShopId = entry.getKey();
//            List<OrderItem> orderItemList = entry.getValue();
//
//            OrderInfo orderInfo = OrderInfo.builder()
//                    .martShopId(martShopId)
//                    .orderState(ORDER_REQUEST.getCode())
//                    .build();
//
//            orderInfoRepository.save(orderInfo);
//
//            orderInfo = orderInfoRepository.findByUserIdxAndMartShopIdAndOrderState(null, martShopId, ORDER_REQUEST.getCode());
//            for(OrderItem orderItem:orderItemList) {
//                orderItem.setOrderInfo(orderInfo);
//                orderItemRepository.save(orderItem);
//            }
//        }
//      ************************************************************
    }

    public OrderInquiryResponse getOrder(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // id에 해당하는 orderinfo 불러오기 ( 준비중 P 인것만)
        Optional<OrderInfo> orderInfoOptional = orderInfoRepository.findByUserAndOrderState(user, ORDER_PREPARE.getCode());

        // 주문이 존재하지 않으면 에러
        OrderInfo orderInfo;

        try {
            orderInfo = orderInfoOptional.get();
        } catch (RuntimeException e) {
            log.info("준비중인 주문이 존재하지 않음, userIdx = {}", userIdx);
            throw new BadRequestException(ORDER_NOT_EXIST);
        }

        // 주문 상품을 response dto 로 전환
        List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);

        List<OrderItemResponse> orderItemResponseList = orderItemList.stream()
                .map(orderItem -> {
                    OrderItemResponse orderItemResponse = OrderItemResponse.of(orderItem);
//                    cartItemResponse.setItemName();
//                    cartItemResponse.setCategoryName();
//                    cartItemResponse.setPrice();
//                    cartItemResponse.setPicName();
                    return orderItemResponse;
                })
                .collect(Collectors.toList());

        MartShop orderMartShop = orderInfo.getMartShop();

        // 나중에 martShopId로 마트 이름 불러오기
        OrderInfoResponse orderInfoResponse = OrderInfoResponse.builder()
                .martShopId(orderMartShop.getMartShopId())
                .martName(orderMartShop.getName())
                .itemCount(orderItemResponseList.size())
                .build();


        log.info("주문 상품 조회 목록 생성, userIdx = {}, orderId = {}", userIdx, orderInfo.getOrderId());

        return OrderInquiryResponse.builder()
                .order(orderInfoResponse)
                .orderItem(orderItemResponseList)
                .build();

//******************************************** 봉인 **************************************************
//        // orderinfo에 해당하는 상품 불러오기
//
//        OrderInquiryResponse orderInquiryResponse = new OrderInquiryResponse();
//        List<OrderItemResponse> resultList = new ArrayList<>();
//
//        if (state.equals("전체")) {
//            // 주문 목록 가져오기
//            // findAll -> findByUserIdx()로 교체
//            List<OrderInfo> orderList = orderInfoRepository.findAll();
//
//            // order에 해당 하는 상품을 dto list에 저장
//            for(OrderInfo orderInfo:orderList){
//                List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);
//                List<OrderItemResponse> orderItemResponseList = orderItemList.stream()
//                        .map((OrderItem orderItem) -> OrderItemResponse.of(orderItem, orderInfo))
//                        .collect(Collectors.toList());
//                resultList.addAll(orderItemResponseList);
//            }
//
//            // resultList
//            // mart 이름 -> 위의 for문에서 넣기, item이름, item 사진 넣기
//
//            orderInquiryResponse.setOrderItem(resultList);
//        } else {
//            // 주문 목록 가져오기
//            // findAll -> findByUserIdxAndOrderState(OrderState.getCodeByState(state))로 교체
//            List<OrderInfo> orderList = orderInfoRepository.findAll();
//
//            // order에 해당 하는 상품을 dto list에 저장
//            for(OrderInfo orderInfo:orderList){
//                List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);
//                List<OrderItemResponse> orderItemResponseList = orderItemList.stream()
//                        .map((OrderItem orderItem) -> OrderItemResponse.of(orderItem, orderInfo))
//                        .collect(Collectors.toList());
//                resultList.addAll(orderItemResponseList);
//            }
//
//            // resultList
//            // mart 이름 -> 위의 for문에서 넣기, item이름, item 사진 넣기
//            orderInquiryResponse.setOrderItem(resultList);
//        }
//        return orderInquiryResponse;
    }
}
