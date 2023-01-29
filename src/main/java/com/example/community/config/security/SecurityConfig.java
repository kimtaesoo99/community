package com.example.community.config.security;

import com.example.community.config.jwt.JwtAccessDeniedHandler;
import com.example.community.config.jwt.JwtAuthenticationEntryPoint;
import com.example.community.config.jwt.JwtSecurityConfig;
import com.example.community.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .antMatchers( "/v3/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()

            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // 시큐리티는 기본적으로 세션을 사용
            // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
            .and()
            .authorizeHttpRequests()
            .antMatchers("/swagger-ui/**", "/v3/**").permitAll() // swagger
            .antMatchers("/api/sign-up", "/api/sign-in", "/api/reissue").permitAll()

            .antMatchers(HttpMethod.GET, "/api/members").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/members/{id}").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/members/{id}").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/members/{id}").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN")

            .antMatchers(HttpMethod.POST, "/api/messages").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/messages/sender").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/messages/sender/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/messages/receiver").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/messages/receiver/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/messages/sender/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/messages/receiver/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .antMatchers(HttpMethod.POST, "/api/boards").authenticated()
            .antMatchers(HttpMethod.GET, "/api/boards/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/boards/likes").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/boards/{id}/favorites").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/boards/favorites").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/boards/search/{keyword}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .antMatchers(HttpMethod.GET, "/api/comments").authenticated()
            .antMatchers(HttpMethod.POST, "/api/comments").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/comments/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .antMatchers(HttpMethod.POST, "/api/reports/members").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/reports/boards").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/reports/comments").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")


            .antMatchers(HttpMethod.GET, "/api/admin/manages/members").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/admin/manages/members/{id}").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/admin/manages/members/{id}").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/admin/manages/boards").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/admin/manages/boards/{id}").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/admin/manages/boards/{id}").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/api/admin/manages/comments").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/admin/manages/comments/{id}").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/admin/manages/comments/{id}").hasAnyAuthority("ROLE_ADMIN")

            .antMatchers(HttpMethod.GET, "/api/categories").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/categories").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.POST, "/api/categories/start").hasAnyAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasAnyAuthority("ROLE_ADMIN")

            .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}

