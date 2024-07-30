package com.backend.martall.domain.owner.service;

import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.entity.OrderItem;
import com.backend.martall.domain.order.repository.OrderInfoRepository;
import com.backend.martall.domain.order.repository.OrderItemRepository;
import com.backend.martall.domain.owner.dto.OwnerDto;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
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

        User user = userRepository.findByUserIdx(userIdx).get();
        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));
        List<OrderInfo> orderInfoList = orderInfoRepository.findByMartShopAndOrderState(martShop, state);

        if(state.equals("P")) {
            orderInfoList.addAll(orderInfoRepository.findTodayCompleteByMartShop(martShop));
        }

        // 대기중(W), 준비중(P), 완료 순(C)
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
}
