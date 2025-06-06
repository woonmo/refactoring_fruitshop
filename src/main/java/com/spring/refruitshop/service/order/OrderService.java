package com.spring.refruitshop.service.order;

import com.spring.refruitshop.domain.common.Address;
import com.spring.refruitshop.domain.order.Order;
import com.spring.refruitshop.domain.order.OrderItem;
import com.spring.refruitshop.domain.order.OrderStatus;
import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.order.*;
import com.spring.refruitshop.repository.order.OrderItemRepository;
import com.spring.refruitshop.repository.order.OrderRepository;
import com.spring.refruitshop.service.cart.CartService;
import com.spring.refruitshop.service.product.ProductService;
import com.spring.refruitshop.service.user.UserService;
import com.spring.refruitshop.util.Pagination;
import com.spring.refruitshop.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;


    // 유효한 유저인지 판별하는 메소드
    private void validationUser(User loginUser) {
        if (loginUser == null || loginUser.getNo() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }
    }// end of private void validationUser(User loginUser) ----------------------


    // 결제 전 주문서를 생성하는 비즈니스 로직
    public OrderDraft prepareOrder(OrderInitRequest request, User loginUser) {
        validationUser(loginUser);

        // 개별 상품 주문상황과 장바구니 주문 상황에 따라 분기한다.
        String draftId = UUID.randomUUID().toString();

        // 개별 상품 주문 상황
        if(request.getProductNo() != null && request.getQuantity() != null) {
            // 1. 상품 정보를 OrderDraftItem 에 저장
            // 한 개의 상품 정보 조회
            Product product = productService.getEntityById(request.getProductNo());
            OrderDraftItem orderDraftItem = new OrderDraftItem(product, request.getQuantity(), false);


            // 2. 주문 정보 입력
            return OrderDraft.builder()
                    .draftId(draftId)
                    .userNo(loginUser.getNo())
                    .email(loginUser.getEmail())
                    .name(loginUser.getName())
                    .tel(loginUser.getTel())
                    .items(List.of(orderDraftItem))        // 불변 LIST 써도 문제? 없을듯 어차피 보여주는 것이니까
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
                                                            CartItemRequest matchedCartItem = request.getCartItemList()
                                                                    .stream()
                                                                    .filter(cartItem -> cartItem.getProductNo().equals(product.getNo()))
                                                                    .findFirst()
                                                                    .orElseThrow(() -> new IllegalArgumentException("장바구니 요청 상품 정보가 없습니다."));

                                                            return new OrderDraftItem(product, matchedCartItem.getQuantity(), matchedCartItem.isFromCart());
                                                        })
                                                        .collect(Collectors.toList());

            log.info("생성된 상품 리스트: {}", productList);

            if (productList.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 상품정보 입니다.");
            }

            // 2. 주문 정보 입력
            return OrderDraft.builder()
                    .draftId(draftId)
                    .userNo(loginUser.getNo())
                    .email(loginUser.getEmail())
                    .name(loginUser.getName())
                    .tel(loginUser.getTel())
                    .items(productList)
                    .zipCode(loginUser.getAddress().getZipcode())
                    .address(loginUser.getAddress().getAddress())
                    .detailAddress(loginUser.getAddress().getDetailAddress())
                    .extraAddress(loginUser.getAddress().getExtraAddress())
                    .build();

        }
        return null;
    }// end of public OrderDraft prepareOrder(OrderInitRequest request, User loginUser) ------------------------


    // 주문확인 후 주문데이터를 처리하는 비즈니스 로직
    @Transactional
    public Order confirmOrder(OrderDraft draft, User loginUser, HttpSession session) {

        validationUser(loginUser);
        // 주문 처리 시 해야할 것
        // 상품 재고 차감, 회원 포인트 증가, 주문 정보 저장 주문 상세 저장, 결제 정보 저장, 배송지 변경 혹은 생성(추후 확장)

        // 1. 회원 정보 조회
        User user = userService.getEntityById(loginUser.getNo());
        user.increasePoint(draft.getPoint());   // 회원의 포인트 증가

        // 2. 주문 정보 생성
        Address address = new Address(draft.getZipCode(), draft.getAddress(), draft.getDetailAddress(), draft.getExtraAddress());
        Order order = Order.builder()
                .user(loginUser)
                .orderDate(LocalDateTime.now())
                .request(draft.getRequestNote())
                .orderTprice((long) draft.getTotalPrice())
                .orderStatus(OrderStatus.PENDING)
                .orderChangeDate(LocalDateTime.now())
                .orderDiscount(draft.getDiscount())
                .paymentPrice(draft.getFinalPrice())
                .receiverName(draft.getReceiverName())
                .receiverTel(draft.getReceiverTel())
                .receiveAddress(address)
                .build();

        // 3. 주문 상품 추가
        for (OrderDraftItem item : draft.getItems()) {
            Product product = productService.getEntityById(item.getNo());   // 상품 하나의 정보 조회
            OrderItem orderItem = OrderItem.createOrderItem(order, product, item.getQuantity(), item.getPrice());   // 주문 상세 row 한 개 정보
            order.addOrderItem(orderItem);
            product.decreaseInventory(item.getQuantity());      // 재고 감소

            // 장바구니 통한 주문의 경우 장바구니에서 상품 제거
            if (item.isFromCart()) {
                cartService.deleteByUserAndProduct(user.getNo(), product.getNo());
            }
        }// end of for() --------------

        order = orderRepository.save(order);
        order.initializeOrderCode(order.getFormattedOrderNo());     // 주문코드 생성

        session.setAttribute("cartCount", cartService.getCountByUserNo(loginUser.getNo()));

        return order;
    }// end of public Long confirmOrder(OrderDraft draft, User loginUser) -------------------------


    // 주문 상세 내용을 반환하는 메소드
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(String orderCode, User loginUser) {
        validationUser(loginUser);

        // 한개 의 주문 정보를 조회
        Order order = orderRepository.findByOrderCodeAndUserNo(orderCode, loginUser.getNo())
                                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 주문 정보를 조회
        List<OrderDetailItem> orderDetailItems = orderItemRepository.findAllByOrderNo(order.getOrderNo())
                                    .stream()
                                    .map(orderItem -> {
                                        return new OrderDetailItem(orderItem);
                                    })
                                    .collect(Collectors.toList());

        log.info("조회된 주문 상세 정보: {}", orderDetailItems);

        return new OrderDetailResponse(order, orderDetailItems);
    }// end of public OrderDetailResponse getOrderDetail(OrderDetailRequest request, User loginUser) --------------------------


    // 회원의 주문 목록을 반환하는 메소드(페이징 처리)
    public OrderListResponse getOrderList(OrderListRequest request, User loginUser) {
        validationUser(loginUser);

        String searchOrderCode = request.getOrderCode().isEmpty() ? null : "%"+request.getOrderCode()+"%";
        LocalDateTime fromDate = request.getFromDate().atStartOfDay();
        LocalDateTime endDate = request.getEndDate().atTime(LocalTime.MAX);
        OrderStatus searchOrderStatus = request.getOrderStatus().isEmpty() ? null : OrderStatus.valueOf(request.getOrderStatus());


        // 검색 조건
        // 시작일 & 끝일 기본, 필터만 검색, 주문코드만 검색, 둘 모두 있는 검색 (총 3가지)

        Pageable pageable = request.toPageable(OrderListRequest.SORT_FIELD_JPQL);
        Page<Order> page = orderRepository.findByOrderDateAndOrderStatusAndOrderCodeContaining(loginUser.getNo(), fromDate, endDate, searchOrderCode, searchOrderStatus, pageable);;

        log.info("회원번호: {}, 주문번호: {}, 검색조건: {}, 시작일: {}, 마지막일: {}", loginUser.getNo(), searchOrderCode, searchOrderStatus, fromDate, endDate);

        Pagination pagination = PagingUtil.getPagination(page, 5);   // 페이징 정보 생성

        List<OrderListItem> orderLists = page.getContent().stream()
                .map(order -> new OrderListItem(order))
                .collect(Collectors.toList());


        return new OrderListResponse(orderLists, pagination);
    }// end of public OrderListResponse getOrderList(OrderListRequest request, User loginUser) ---------------------------


    // 주문 상태를 변경해주는 메소드
    @Transactional
    public void updateOrderStatus(UpdateOrderStatusRequest request, User loginUser) {
        validationUser(loginUser);

        Order order = orderRepository.findByOrderCode(request.getOrderCode())
                                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문입니다."));

        order.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
    }// end of public boolean updateOrderStatus(UpdateOrderStatusRequest request, User loginUser) -------------------
}
