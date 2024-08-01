package com.backend.martall.domain.owner.service;

import com.backend.martall.domain.image.service.ImageService;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import com.backend.martall.domain.item.repository.ItemRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final ImageService imageService;
    private final ItemRepository itemRepository;


    // 주문 내역 조회
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


    // 주문 상태 수정
    @Transactional
    public OwnerDto.OrderStateUpdateResponseDto updateOrderState(OwnerDto.OrderStateUpdateRequestDto orderStateUpdateRequestDto,
                                                                 Long userIdx) {

        Long orderId = orderStateUpdateRequestDto.getOrderId();
        String orderState = orderStateUpdateRequestDto.getOrderState();

        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        // 마트에 해당 하는 주문인지 확인
        if(!orderInfoRepository.existsByOrderIdAndMartShop(orderId, martShop)) {
            throw new BadRequestException(ResponseStatus.OWNER_WRONG_ORDER);
        }

        // 변경하려는 상태가 존재하는지 확인
        if(!OrderState.isValidState(orderState)) {
            throw new BadRequestException(ResponseStatus.OWNER_WRONG_ORDER_STATE);
        }

        // 입력 받은 정보로 orderInfo 업데이트
        if(orderInfoRepository.updateStateById(orderId, orderState) == 0) {
            throw new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_ORDER);
        }

        return OwnerDto.OrderStateUpdateResponseDto.builder()
                .orderId(orderId)
                .orderState(orderState)
                .build();
    }


    // 주문 상세 내역 조회
    public OwnerDto.OrderDetailResponseDto getOrderDetail(OwnerDto.OrderDetailRequestDto orderDetailRequestDto,
                                                          Long userIdx) {

        Long orderId = orderDetailRequestDto.getOrderId();

        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        // 마트에 해당 하는 주문인지 확인
        if(!orderInfoRepository.existsByOrderIdAndMartShop(orderId, martShop)) {
            throw new BadRequestException(ResponseStatus.OWNER_WRONG_ORDER);
        }

        // 주문
        OrderInfo orderInfo = orderInfoRepository.findById(orderId).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_ORDER));

        // 주문 상품 목록
        List<OrderItem> orderItemList = orderItemRepository.findByOrderInfo(orderInfo);

        // 주문한 회원
        User customer = orderInfo.getUser();

        List<OwnerDto.OrderDetailResponseDto.Item> itemList = new ArrayList<>();

        int sumPrice = 0;

        // 총 가격 계산 및 item dto 리스트 생성
        for (OrderItem orderItem : orderItemList) {
            Item item = orderItem.getItem();
            int itemPrice = orderItem.getCount() * item.getPrice();
            sumPrice += itemPrice;

            itemList.add(OwnerDto.OrderDetailResponseDto.Item.builder()
                    .productName(item.getItemName())
                    .quantity(orderItem.getCount())
                    .price(itemPrice)
                    .build());
        }

        return OwnerDto.OrderDetailResponseDto.builder()
                .customerName(customer.getUsername())
                .regularState(martBookmarkRepository.existsByUserAndMartShop(customer, orderInfo.getMartShop()))
                .orderAt(orderInfo.getCreatedAt())
                .itemList(itemList)
                .sumPrice(sumPrice)
                .build();
    }

    @Transactional
    public OwnerDto.ItemResponseDto registerItem(MultipartFile profile,
                                                      MultipartFile content,
                                                      OwnerDto.ItemCreateRequestDto itemCreateRequestDto,
                                                      Long userIdx) {

        // profile, content가 null인 경우 처리
        if (profile == null || profile.isEmpty() || content == null || content.isEmpty()) {
            throw new BadRequestException(ResponseStatus.OWNER_NEED_IMAGE);
        }

        // 이미지 s3에 저장하고 url 저장
        String profileUrl = imageService.saveImage(profile);
        String contentUrl = imageService.saveImage(content);

        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        // 상품 빌드
        Item item = Item.builder()
                .itemName(itemCreateRequestDto.getItemName())
                .categoryId(ItemCategory.findByName(itemCreateRequestDto.getItemCategory()))
                .price(itemCreateRequestDto.getPrice())
                .profilePhoto(profileUrl)
                .content(contentUrl)
                .inventoryQuantity(9999)
                .martShop(martShop)
                .build();

        itemRepository.save(item);

        return OwnerDto.ItemResponseDto.builder()
                .itemId(item.getItemId())
                .build();
    }


    @Transactional
    public OwnerDto.ItemResponseDto updateItem(MultipartFile profile,
                                                 MultipartFile content,
                                                 OwnerDto.ItemUpdateRequestDto itemUpdateRequestDto,
                                                 Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        Item item = itemRepository.findByItemId(itemUpdateRequestDto.getItemId()).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        // 마트에 상품이 존재하는지 검증
        if (!item.getMartShop().equals(martShop)) {
            throw new BadRequestException(ResponseStatus.OWNER_WRONG_Item);
        }

        String profileUrl = null;
        String contentUrl = null;

        if (profile != null && !profile.isEmpty()) {
            profileUrl = imageService.saveImage(profile);
        }

        if (content != null && !content.isEmpty()) {
            contentUrl = imageService.saveImage(content);
        }

        item.updateItem(profileUrl, contentUrl, itemUpdateRequestDto);

        itemRepository.save(item);

        return OwnerDto.ItemResponseDto.builder()
                .itemId(item.getItemId())
                .build();
    }

    @Transactional
    public OwnerDto.MartExposureResponseDto updateMartExposure(OwnerDto.MartExposureRequestDto martExposureRequestDto,
                                                               Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findByUser(user).orElseThrow(() -> new BadRequestException(ResponseStatus.OWNER_NOT_EXIST_MART));

        martShop.updateExposure(martExposureRequestDto.getExposure());

        martRepository.save(martShop);

        return OwnerDto.MartExposureResponseDto.builder()
                .exposure(martShop.getExposure())
                .build();
    }
}
