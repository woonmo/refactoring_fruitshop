package com.spring.refruitshop.config;

import com.spring.refruitshop.service.cart.CartService;
import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final CartService cartService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.png","/static/**", "/css/**", "/js/**", "/images/**", "/bootstrap-4.6.2-dist/**");       // static 아래
    }

    // filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrfConfig) -> csrfConfig.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests((auth) -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/", "/login", "/signUp", "/api/users", "/iframe/agree.html", "/api/users/duplicate", "/api/users/me").permitAll()
                        .requestMatchers("/products/**", "/product/**", "/h2-console/**").permitAll()
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")    // 관리자만 접근 가능하도록
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/login")
                        .usernameParameter("userid")
                        .passwordParameter("password")
//                        .loginProcessingUrl("login")    // 로그인 데이터 처리를 위함
                        .failureUrl("/login?loginFail=true")    // 로그인 실패 시
                        .successHandler(myAuthenticationSuccessHandler(userService, cartService))
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                .headers((headerconfig) -> headerconfig
                        .frameOptions((frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(((request, response, authException) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || request.getRequestURI().startsWith("/api")) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            }
                            else {
                                response.sendRedirect("/login");
                            }
                })));

        return http.build();
    }// end of public SecurityFilterChain filterChain(HttpSecurity http) throws Exception ------------------


    // 프론트와 통신 시 같은 도메인 CORS 방지
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://fruitshop.kro.kr"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // == login을 성공하면 연이어서 처리해야할 작업을 해주는 bean 생성하기 == //
    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler(UserService userService, CartService cartService) {
        return new MyAuthenticationSuccessHandler(userService, cartService);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
