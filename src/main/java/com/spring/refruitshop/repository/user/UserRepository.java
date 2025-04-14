package com.spring.refruitshop.repository.user;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(@NotBlank(message = "아이디는 필수 입력사항입니다 !!") @Size(min = 5, max = 20, message = "아이디는 5~20 글자이어야 합니다.") String userid);

    boolean existsByEmail(@NotBlank(message = "이메일은 필수 입력사항입니다 !!") @Email(message = "유효한 이메일 형식을 입력해주세요 !!") String email);

//    Optional<User> findByIdAndRole(Long no, String role);   // 유저의 롤을 검색해보는 메소드
}
