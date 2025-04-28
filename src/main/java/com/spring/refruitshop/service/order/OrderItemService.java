package com.spring.refruitshop.service.order;

import com.spring.refruitshop.domain.order.ShipStatus;
import com.spring.refruitshop.dto.user.MyPageShipItem;
import com.spring.refruitshop.repository.order.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }



    // 회원번호로 회원의 주문상품 배송상태를 반환하는 메소드
    public List<MyPageShipItem> findShipStatusCount(Long no) {
        List<Object[]> resultList = orderItemRepository.findShipStatusCounts(no);


        // 기본 세팅 0 값으로 설정
        Map<String, Integer> statusMap = Arrays.stream(ShipStatus.values())
                                    .collect(Collectors.toMap(
                                            ShipStatus::getDescription,
                                            status -> 0,
                                            (oldValue, newValue) -> oldValue,   // 병합함수 (중복 방지)
                                            LinkedHashMap::new                                  // LinkedHashMap 으로 사용, enum 순서가 map 의 순서
                                    ));

        // 쿼리 결과 업데이트
        for (Object[] result : resultList) {
            ShipStatus shipStatus = (ShipStatus) result[0];
            int count = ((Number) result[1]).intValue();
            String description = shipStatus.getDescription();
            statusMap.put(description, count);
        }// end of for() -----------------


        return statusMap.entrySet().stream()
                .map(entry -> new MyPageShipItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

    }// end of public List<MyPageShipItem> findShipStatusCount(Long no) -----------------------
}
