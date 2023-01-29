package com.example.community.controller.category;


import com.example.community.dto.category.CategoryCreateRequestDto;
import com.example.community.service.category.CategoryService;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    @InjectMocks
    CategoryController categoryController;

    @Mock
    CategoryService categoryService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }


    @Test
    public void 전체카테고리_조회_테스트() throws Exception {
        // given

        // when, then
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk());
        verify(categoryService).findAllCategory();
    }

    @Test
    public void 카테고리생성_테스트() throws Exception {
        // given
        CategoryCreateRequestDto req = new CategoryCreateRequestDto("category1", 1l);

        // when, then
        mockMvc.perform(
                post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());

        verify(categoryService).createCategory(req);
    }

    @Test
    public void 카테고리삭제_테스트() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id))
            .andExpect(status().isOk());
        verify(categoryService).deleteCategory(id);
    }
}
