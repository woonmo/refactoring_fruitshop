package com.spring.refruitshop.service.order;

import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.order.CartItemRequest;
import com.spring.refruitshop.dto.order.OrderDraft;
import com.spring.refruitshop.dto.order.OrderDraftItem;
import com.spring.refruitshop.dto.order.OrderInitRequest;
import com.spring.refruitshop.repository.order.OrderRepository;
import com.spring.refruitshop.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }


    // 결제 전 주문서를 생성하는 비즈니스 로직
    public OrderDraft prepareOrder(OrderInitRequest request, User loginUser) {
        if (loginUser == null || loginUser.getNo() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }


        // 개별 상품 주문상황과 장바구니 주문 상황에 따라 분기한다.

        // 개별 상품 주문 상황
        if(request.getProductNo() != null && request.getQuantity() != null) {
            // 1. 상품 정보를 OrderDraftItem 에 저장
            // 한 개의 상품 정보 조회
            Product product = productService.getEntityById(request.getProductNo());
            OrderDraftItem orderDraftItem = new OrderDraftItem(product, request.getQuantity());

            // 2. 주문회원 정보 입력
            return OrderDraft.builder()
                    .draftId(UUID.randomUUID().toString())
                    .userNo(loginUser.getNo())
                    .email(loginUser.getEmail())
                    .items(List.of(orderDraftItem))        // 불변 LIST 써도 문제? 없을듯 어차피 보여주는 것이니까
                    .receiverName(loginUser.getUsername())
                    .receiverTel(loginUser.getTel())
                    .zipCode(loginUser.getAddress().getZipcode())
                    .address(loginUser.getAddress().getAddress())
                    .detailAddress(loginUser.getAddress().getDetailAddress())
                    .extraAddress(loginUser.getAddress().getExtraAddress())
                    .build();
        }


        // 장바구니 주문 상황
        if(request.getCartItemList() != null && request.getCartItemList().size() > 0) {
            List<Long> productNoList = request.getCartItemList()
                    .stream()
                    .map(CartItemRequest::getProductNo)
                    .collect(Collectors.toList());


            // 1. 상품 정보를 조회 해온다.
            List<OrderDraftItem> productList = productService.getEntityListByIds(productNoList)
                                                        .stream()
                                                        .map(product -> {
                                                            Integer quantity = request.getCartItemList()
                                                                    .stream()
                                                                    .filter(cartItem -> cartItem.getProductNo().equals(product.getNo()))
                                                                    .findFirst()
                                                                    .map(CartItemRequest::getQuantity)
                                                                    .orElseThrow(() -> new IllegalArgumentException("수량 정보가 없습니다."));

                                                            return new OrderDraftItem(product, quantity);
                                                        })
                                                        .collect(Collectors.toList());

            if (productList == null || productList.size() == 0) {
                throw new IllegalArgumentException("유효하지 않은 상품정보 입니다.");
            }

            return OrderDraft.builder()
                    .draftId(UUID.randomUUID().toString())
                    .userNo(loginUser.getNo())
                    .email(loginUser.getEmail())
                    .items(productList)        // 불변 LIST 써도 문제? 없을듯 어차피 보여주는 것이니까
                    .receiverName(loginUser.getUsername())
                    .receiverTel(loginUser.getTel())
                    .zipCode(loginUser.getAddress().getZipcode())
                    .address(loginUser.getAddress().getAddress())
                    .detailAddress(loginUser.getAddress().getDetailAddress())
                    .extraAddress(loginUser.getAddress().getExtraAddress())
                    .build();

        }
        return null;
    }// end of public OrderDraft prepareOrder(OrderInitRequest request, User loginUser) ------------------------
}
