package com.example.community.controller.admin;

import com.example.community.service.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @InjectMocks
    AdminController adminController;

    @Mock
    AdminService adminService;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();


    }

    @Test
    public void 정지된_유저목록조회_테스트() throws Exception {
        // given

        // when, then
        mockMvc.perform(
                get("/api/admin/manages/members"))
            .andExpect(status().isOk());
        verify(adminService).findAllReportedMember();
    }

    @Test
    public void 정지된_게시글조회_테스트() throws Exception {
        // given

        // when, then
        mockMvc.perform(
                get("/api/admin/manages/boards"))
            .andExpect(status().isOk());
        verify(adminService).findAllReportedBoards();
    }

    @Test
    public void 정지된_댓글조회_테스트() throws Exception {
        // given

        // when, then
        mockMvc.perform(
                get("/api/admin/manages/comments"))
            .andExpect(status().isOk());
        verify(adminService).findAllReportedComments();
    }


    @Test
    public void 신고된유저_정지해제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                post("/api/admin/manages/members/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).unlockMember(id);
    }

    @Test
    public void 신고된게시글_정지해제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                post("/api/admin/manages/boards/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).unlockBoard(id);
    }

    @Test
    public void 신고된댓글_정지해제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                post("/api/admin/manages/comments/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).unlockComment(id);
    }



    @Test
    public void 신고된유저_삭제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/admin/manages/members/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).deleteReportedMember(id);
    }

    @Test
    public void 신고된게시글_삭제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/admin/manages/boards/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).deleteReportedBoard(id);
    }

    @Test
    public void 신고된댓글_삭제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/admin/manages/comments/{id}", id))
            .andExpect(status().isOk());
        verify(adminService).deleteReportedComment(id);
    }
}
