package com.spring.refruitshop.repository.user;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.domain.user.UserRole;
import com.spring.refruitshop.dto.user.UserIdEmailOnly;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(@NotBlank(message = "아이디는 필수 입력사항입니다 !!") @Size(min = 5, max = 20, message = "아이디는 5~20 글자이어야 합니다.") String userid);

    boolean existsByEmail(@NotBlank(message = "이메일은 필수 입력사항입니다 !!") @Email(message = "유효한 이메일 형식을 입력해주세요 !!") String email);

    Optional<User> findByUserId(String userid);

    Optional<User> findByEmail(String email);

    @Query("SELECT u.userId AS userId, u.email AS email FROM User u WHERE u.userId = :userId OR u.email = :email")  // Project 인터페이스 사용 시 AS 꼭 해줘야 인식함!! getUserID(), getEmail()
    List<UserIdEmailOnly> findByUserIdOrEmail(@Param("userId") String userId, @Param("email") String email);    // 회원가입 시 id & email 중복검사 용
}
