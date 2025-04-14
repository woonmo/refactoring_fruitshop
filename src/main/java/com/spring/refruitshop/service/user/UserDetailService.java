package com.spring.refruitshop.service.user;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public User loadUserByUsername (String userid) {
        return userRepository.findByUserId(userid)
                .orElseThrow(() -> new IllegalArgumentException(userid + " not found"));
    }// end of public User loadUserByUsername (String email) --------
}
