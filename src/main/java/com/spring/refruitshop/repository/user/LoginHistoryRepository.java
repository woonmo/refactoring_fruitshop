package com.spring.refruitshop.repository.user;

import com.spring.refruitshop.domain.user.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
