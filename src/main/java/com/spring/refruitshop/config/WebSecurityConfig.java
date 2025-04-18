package com.spring.refruitshop.config;

import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())         // 데이터 베이스
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/bootstrap-4.6.2-dist/**");       // static 아래
    }

    // filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrfConfig) -> csrfConfig.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests((auth) -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/", "/login", "/signup", "/api/users").permitAll()
                        .requestMatchers("/products/**", "/product/**").permitAll()
                        .requestMatchers("/adimin/**", "/api/admin/**").hasRole("ADMIN")    // 관리자만 접근 가능하도록
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/login")
                        .usernameParameter("userid")
                        .passwordParameter("password")
//                        .loginProcessingUrl("login")    // 로그인 데이터 처리를 위함
                        .failureUrl("/login?loginFail=true")    // 로그인 실패 시
                        .successHandler(myAuthenticationSuccessHandler(userService))
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                .headers((headerconfig) -> headerconfig
                        .frameOptions((frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                );

        return http.build();
    }// end of public SecurityFilterChain filterChain(HttpSecurity http) throws Exception ------------------


    // == login을 성공하면 연이어서 처리해야할 작업을 해주는 bean 생성하기 == //
    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler(UserService userService) {
        return new MyAuthenticationSuccessHandler(userService);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
