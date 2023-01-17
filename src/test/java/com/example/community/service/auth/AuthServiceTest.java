package com.example.community.service.auth;

import com.example.community.config.jwt.TokenProvider;
import com.example.community.dto.sign.LoginRequestDto;
import com.example.community.dto.sign.SignUpRequestDto;
import com.example.community.exception.LoginFailureException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.refreshToken.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    AuthService authService;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void beforeEach() {
        authService = new AuthService(authenticationManagerBuilder, memberRepository, passwordEncoder, tokenProvider, refreshTokenRepository);
    }

    @Test
    void 회원가입_테스트() {
        // given
        SignUpRequestDto req = new SignUpRequestDto("username", "1234", "name");

        // when
        authService.signup(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void 로그인실패_테스트() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("email", "password")))
            .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void 비밀번호_검증_테스트() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("username", "password")))
            .isInstanceOf(LoginFailureException.class);
    }
}
