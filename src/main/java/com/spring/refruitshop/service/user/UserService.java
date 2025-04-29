package com.spring.refruitshop.service.user;

import com.spring.refruitshop.dto.user.*;
import com.spring.refruitshop.domain.user.LoginHistory;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.user.LoginHistoryRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import com.spring.refruitshop.service.order.OrderItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginHistoryRepository loginHistoryRepository;
    private final OrderItemService orderItemService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoginHistoryRepository loginHistoryRepository, OrderItemService orderItemService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginHistoryRepository = loginHistoryRepository;
        this.orderItemService = orderItemService;
    }

    // 회원 가입
    @Transactional
    public UserRegisterResponse save(UserRegisterRequest request) {
        // 아이디 중복 검사
        if (userRepository.existsByUserId(request.getUserid())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 패스워드 암호화
        request.encryptPassword(passwordEncoder.encode(request.getPassword()));

        // 회원 등록
        User user = userRepository.save(request.toEntity());
        log.info("Created User: {}", user);

        return new UserRegisterResponse(user);
    }// end of public User save(UserRegisterRequest request) -----------------


    // 로그인 처리
    // 시큐리티 적용시 수정 필요
//    public LoginUser login(HttpServletRequest request, LoginRequest loginRequest) {
//
//        log.info("Login request: {}", loginRequest.getUserid());
//
//        User user = userRepository.findByUserId(loginRequest.getUserid())
//                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 틀립니다."));
//
//        if (!user.getPassword().equals(loginRequest.getPassword())) {
//            throw new IllegalArgumentException("아이디 혹은 비밀번호가 틀립니다.");
//        }
//
//        log.info("Login User: {}", user);
//        LoginUser loginUser = new LoginUser(user);
//
//        request.getSession().setAttribute("user", loginUser);
//        return loginUser;
//    }// end of public void login(HttpServletRequest request, LoginRequest loginRequest) -------------------


    // userid 로 회원을 조회하는 메소드
    public User findByUserId(String userid) {
        User user = userRepository.findByUserId(userid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 USERID 입니다."));
        return user;
    }// end of public User findByUserId(String userid) ------------------


    // 로그인 기록을 저장
    @Transactional
    public void addLoginHistory(User loginUser, String clientIp) {
        LoginHistory loginHistory = loginHistoryRepository.save(
                LoginHistory.builder()
                        .user(loginUser)
                        .clientIp(clientIp)
                        .build()
        );
        log.info("Login : {}", loginHistory);

    }// end of public void addLoginHistory(User loginUser) ----------------------


    // 회원 엔티티를 반환하는 메소드(비즈니스 로직용)
    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));
    }// end of public User getEntityById(Long id) --------------------


    // 회원가입 시 userid, email 중복 검사
    @Transactional(readOnly = true)
    public UserDuplicateResponse duplicateCheckUserIdAndEmail(UserDuplicateRequest request) {

        String userId = request.getUserId() == null ? "" : request.getUserId();
        String email  = request.getEmail()  == null ? "" : request.getEmail();

        log.info("중복검사 입력 userId: {}, email: {}", userId, email);

        List<UserIdEmailOnly> resultList = userRepository.findByUserIdOrEmail(userId, email);

        boolean userIdExist = resultList.stream()
                .anyMatch(result -> userId.equals(result.getUserId()));
        boolean emailExist = resultList.stream()
                .anyMatch(result -> email.equals(result.getEmail()));

        return new UserDuplicateResponse(userIdExist, emailExist);
    }// end of public UserDuplicateResponse duplicateCheckUserIdAndEmail(UserDuplicateRequest request) -----------------


    // 회원의 쿠폰, 상품배송 상태를 반환하는 메소드(마이페이지용)
    @Transactional(readOnly = true)
    public MyPageInfoResponse findCouponAndDeliveryStatus(User loginUser) {
        List<MyPageShipItem> shipStatusCount = orderItemService.findShipStatusCount(loginUser.getNo());

        return new MyPageInfoResponse(shipStatusCount, null);
    }// end of public MyPageInfoResponse findCouponAndDeliveryStatus(User loginUser) ---------------------


    // 회원 정보를 수정하는 메소드
    @Transactional
    public LoginUser updateUserInfo(UpdateUserInfoRequest request, User loginUser) {

        log.info("수정 전 회원정보: {}", request);

        // 회원 정보를 조회 후 정보를 변경한다.
        User user = userRepository.findByUserId(request.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        // 로그인을 하지 않았거나 다른 유저의 정보를 수정요청한 경우
        if (loginUser == null || user.getNo() != loginUser.getNo()) {
            throw new IllegalArgumentException("다른 회원의 정보는 수정할 수 없습니다.");
        }

        // 패스워드 변경 요청 시 암호화
        if (StringUtils.hasText(request.getPassword())) {
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }

        user.updateInfo(request);

        return new LoginUser(user);
    }// end of public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest request, User loginUser) -------------------


    // 비밀번호 변경 페이지에서 비밀번호 변경
    @Transactional
    public boolean changePassword(String newPassword, User loginUser) {

        User user = userRepository.findById(loginUser.getNo())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));

        log.info("비밀번호 변경 요청, 전달받은 새로운 비밀번호: {}", newPassword);

        // 새 비밀번호와 기존 비밀번호가 같은지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            log.info("기존 비밀번호와 같으므로 비밀번호 변경 불가");
            return false;
        }

        // 새 비밀번호로 변경
        user.updatePassword(passwordEncoder.encode(newPassword));
        log.info("비밀번호 변경 성공! 변경된 비밀번호: {}", user.getPassword());
        return true;
    }// end of public boolean checkAlreadyUsePassword(String newPassword, User loginUser) -----------------
}
