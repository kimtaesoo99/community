package com.example.community.controller.auth;


import com.example.community.dto.sign.LoginRequestDto;
import com.example.community.dto.sign.SignUpRequestDto;
import com.example.community.dto.sign.TokenResponseDto;
import com.example.community.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    AuthController authController;
    @Mock
    AuthService authService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void 회원가입_테스트() throws Exception{
        //given
        SignUpRequestDto req = new SignUpRequestDto("test", "test1", "1234");

        //when
        mockMvc.perform(
            post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        //then
        verify(authService).signup(req);
    }

    @Test
    public void 로그인_테스트() throws Exception {
        // given
        LoginRequestDto req = new LoginRequestDto("test123", "test");
        given(authService.signIn(req)).willReturn(new TokenResponseDto("access", "refresh"));

        // when, then
        mockMvc.perform(
                post("/api/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.data.accessToken").value("access"))
            .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        verify(authService).signIn(req);
    }

    @Test
    void 널값무시_테스트() throws Exception {
        // 응답결과로 반환되는 JSON 문자열이 올바르게 제거되는지 검증
        // given
        SignUpRequestDto req = new SignUpRequestDto("test", "1234", "name");

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.result").doesNotExist());
    }
}
