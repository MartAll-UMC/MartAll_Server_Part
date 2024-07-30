package com.backend.martall.domain.owner.service;

import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.entity.OrderItem;
import com.backend.martall.domain.order.entity.OrderState;
import com.backend.martall.domain.order.repository.OrderInfoRepository;
import com.backend.martall.domain.order.repository.OrderItemRepository;
import com.backend.martall.domain.owner.dto.OwnerDto;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerService {

    private final UserRepository userRepository;
    private final MartRepository martRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final MartBookmarkRepository martBookmarkRepository;
    private final OrderItemRepository orderItemRepository;


    public OwnerDto.OrderListResponseDto getOrderList(String state, Long userIdx) {

        // 로그인한 유저(사장) 의 마트 정보 및 주문 내역 불러오기
        User user = userRepository.findByUserIdx(userIdx).get();
        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));
        List<OrderInfo> orderInfoList = orderInfoRepository.findByMartShopAndOrderState(martShop, state);

        // 준비중(P)이면 당일 완료(C) 주문도 추가로 불러오기
        if(state.equals("P")) {
            orderInfoList.addAll(orderInfoRepository.findTodayCompleteByMartShop(martShop));
        }

        // 대기중(W), 준비중(P), 완료 순(C)
        // dto 생성
        return OwnerDto.OrderListResponseDto.builder()
                .wCount(orderInfoRepository.countByMartShopAndOrderState(martShop, "W"))
                .pCount(orderInfoRepository.countByMartShopAndOrderState(martShop, "P"))
                .cCount(orderInfoRepository.countByMartShopAndOrderState(martShop, "C"))
                .order(orderInfoList.stream()
                        .map(orderInfo -> {
                            User orderUser = orderInfo.getUser();
                            List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);
                            String firstItemName = orderItemList.get(0).getItem().getItemName();
                            int otherItemsCount = orderItemList.size() - 1;
                            String orderName = otherItemsCount > 0 ? firstItemName + " 외 " + otherItemsCount + "개" : firstItemName;

                            return OwnerDto.OrderListResponseDto.Order.builder()
                                    .orderId(orderInfo.getOrderId())
                                    .customerName(orderUser.getUsername())
                                    .regularState(martBookmarkRepository.existsByUserAndMartShop(orderUser, martShop))
                                    .orderAt(orderInfo.getCreatedAt())
                                    .orderName(orderName)
                                    .sumPrice(orderItemList.stream()
                                            .mapToLong(orderItem -> (long) orderItem.getItem().getPrice() * orderItem.getCount())
                                            .sum())
                                    .state(orderInfo.getOrderState())
                                    .build();
                        })
                        .toList())
                .build();
    }


    @Transactional
    public OwnerDto.OrderStateUpdateResponseDto updateOrderState(OwnerDto.OrderStateUpdateRequestDto orderStateUpdateRequestDto) {
        Long orderId = orderStateUpdateRequestDto.getOrderId();
        String orderState = orderStateUpdateRequestDto.getOrderState();

        if(!OrderState.isValidState(orderState)) {
            throw new BadRequestException(ResponseStatus.OWNER_WRONG_ORDER_STATE);
        }

        // 입력받은 정보로 orderInfo 업데이트
        if(orderInfoRepository.updateStateById(orderId, orderState) == 0) {
            throw new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_ORDER);
        }

        return OwnerDto.OrderStateUpdateResponseDto.builder()
                .orderId(orderId)
                .orderState(orderState)
                .build();
    }
}
