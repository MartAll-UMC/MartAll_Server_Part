package com.backend.martall.domain.order.service;

import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.repository.OrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.backend.martall.domain.order.entity.OrderState.ORDER_COMPLETE;

@Service
@RequiredArgsConstructor
public class OrderAsyncService {

    private final OrderInfoRepository orderInfoRepository;

    @Async
    public void changeOrderState(OrderInfo orderInfo) {
        try{
            Thread.sleep(30000);
            orderInfo.updateOrderState(ORDER_COMPLETE);
            orderInfoRepository.save(orderInfo);
        } catch (InterruptedException e) {

        }
    }
}
