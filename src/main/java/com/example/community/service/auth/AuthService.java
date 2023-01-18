package com.example.community.service.auth;

import com.example.community.config.jwt.TokenProvider;
import com.example.community.dto.sign.*;
import com.example.community.domain.member.Authority;
import com.example.community.domain.member.Member;
import com.example.community.domain.member.RefreshToken;
import com.example.community.exception.LoginFailureException;
import com.example.community.exception.UsernameAlreadyExistsException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignUpRequestDto req) {
        validateSignUpInfo(req);
        Member member = createSignupFormOfUser(req);
        memberRepository.save(member);
    }

    @Transactional
    public TokenResponseDto signIn(LoginRequestDto req) {
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(() -> {
            throw new LoginFailureException();
        });

        validatePassword(req, member);
        Authentication authentication = getUserAuthentication(req);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = buildRefreshToken(authentication, tokenDto);
        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private RefreshToken buildRefreshToken(Authentication authentication, TokenDto tokenDto) {
        return RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();
    }

    private Authentication getUserAuthentication(LoginRequestDto req) {
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        validateRefreshToken(tokenRequestDto);

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        validateRefreshTokenOwner(refreshToken, tokenRequestDto);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

    private Member createSignupFormOfUser(SignUpRequestDto req) {
        Member member = Member.builder()
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .name(req.getName())
            .authority(Authority.ROLE_USER)
            .build();
        return member;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UsernameAlreadyExistsException(signUpRequestDto.getUsername());
        }
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private void validateRefreshToken(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private void validateRefreshTokenOwner(RefreshToken refreshToken, TokenRequestDto tokenRequestDto) {
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}
