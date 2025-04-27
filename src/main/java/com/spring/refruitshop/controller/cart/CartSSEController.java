package com.spring.refruitshop.controller.cart;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Slf4j
public class CartSSEController {

    private final HttpSession session;

    public CartSSEController (HttpSession session) {
        this.session = session;
    }

    private final List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();  // CopyOnWriteArrayList 는 스레드 안정성을 제공


    // 클라이언트 측으로 장바구니 개수를 전송하는 메소드
    @GetMapping(value = "/api/carts/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  // SSE 스트림을 생성하는 엔트포인트
    public ResponseEntity<SseEmitter> subscribeCartCount() throws IOException {

        // 비동기적으로 이벤트를 클라이언트에게 전송하는데에 사용하는 MVC 컴포넌트
        SseEmitter emitter = new SseEmitter(3600_000L);        // 타임아웃 설정(1시간), 밀리초단위


        // 헤더 캐시 비활성화
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        headers.set("X-Accel-Buffering", "no");


        emitterList.add(emitter);

        emitter.onCompletion(() -> emitterList.remove(emitter));
        emitter.onTimeout(() -> emitterList.remove(emitter));
        emitter.onError(throwable -> emitterList.remove(emitter));

        // 최초 연결 시 현재 장바구니 개수 보내기
        sendCartCount(emitter, getCartCount());

        log.info("SSE 최초연결 시도");

        return ResponseEntity.ok().headers(headers).body(emitter);
    }// end of public ResponseEntity<SseEmitter> sendCartCount() --------------------


    // 실제 클라이언트로 전송되는 부분
    private void sendCartCount(SseEmitter emitter, int cartCount) throws IOException {

        List<SseEmitter> deadEmitterList = new CopyOnWriteArrayList<>();

        emitter.send(SseEmitter.event()
                .name("message")   // 이벤트 이름 (클라이언트에서 핸들링 할 때 사용 event.onmessage  이름 다르게할경우 cartUpdate -> 이벤트 리스너 활용 addEventListener('cartUpdate')
                .data(String.format("{\"cartCount\": %d}", cartCount)));

        log.info("SSE 클라이언트 메시지 전송 시도, 보낸 장바구니 개수: {}", cartCount);
    }// end of private void sendCartCount(SseEmitter emitter, int cartCount) ---------------


    // 세션에서 장바구니 개수를 가져온다.
    private int getCartCount() {
        return (int) session.getAttribute("cartCount");
    }// end of private int getCartCount() -------------


    // 장바구니 정보가 변경될 때 호출되는 메소드
    public void notifyCartUpdate() {
        int currentCartCount = getCartCount();

        List<SseEmitter> deadEmitterList = new CopyOnWriteArrayList<>();

        emitterList.forEach(emiter -> {
            try {
                sendCartCount(emiter, currentCartCount);
            } catch (IOException e) {
                deadEmitterList.add(emiter);
            }
        });

        emitterList.remove(deadEmitterList);
    }// end of public void notifyCartUpdate() -----------------------

}
