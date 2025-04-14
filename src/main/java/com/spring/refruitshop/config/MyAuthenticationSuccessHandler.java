package com.spring.refruitshop.config;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.domain.user.UserStatus;
import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

import static com.spring.refruitshop.util.IpUtil.getClientIp;

@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;


    // 로그인을 성공했을 시 처리 핸들러
    @SuppressWarnings("unused")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication)	// Authentication authentication 는 로그인 된 사람(user)이다.
            throws IOException, ServletException {

        // 세션
        HttpSession session = request.getSession();

        // 로그인 한 유저 정보를 가져온다.
        User loginUser = (User) authentication.getPrincipal();  // 로그인 한 유저 객체, UserDetails 객체 UserDetailService 에서 구현했음
        session.setAttribute("loginUser", loginUser);


        String redirectUrl = (String)session.getAttribute("prevURLPage");

        // 로그인 기록 테이블에 저장, 휴면, 비번변경 여부 판단
        if (loginUser.getStatus() == UserStatus.IDLE) {
            // 휴면 유저일 경우
            redirectUrl = "/idle";
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
        else {
            String clientIp = getClientIp(request);  // 접속 IP 가져오기

            try {
                userService.addLoginHistory(loginUser, clientIp);   // 로그인 정보 기록
            } catch (Exception e) {
                log.error("Failed to add login history for user: {}", loginUser.getUserId());
            }


            // 이전 페이지로 이동
            if (redirectUrl != null) {
                session.removeAttribute("prevURLPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }
            else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }//

    }// end of public void onAuthenticationSuccess (...) -------------------

}
