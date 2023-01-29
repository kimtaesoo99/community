package com.example.community.service.category;

import com.example.community.domain.category.Category;
import com.example.community.dto.category.CategoryCreateRequestDto;
import com.example.community.dto.category.CategoryResponseDto;
import com.example.community.repository.category.CategoryRepository;
import com.example.community.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    MemberRepository memberRepository;


    @Test
    public void 카테고리전체_조회_테스트() {
        // given
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("categoty",null));
        given(categoryRepository.findAllOrderByParent()).willReturn(categories);

        // when
        List<CategoryResponseDto> result = categoryService.findAllCategory();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 카테고리생성_테스트() {
        // given
        Category category = new Category("s", null);
        CategoryCreateRequestDto req = new CategoryCreateRequestDto("name", 1l);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.createCategory(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    public void 카테고리삭제_테스트() {
        // given
        Category category = new Category("s", null);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(anyLong());

        // then
        verify(categoryRepository).delete(any());
    }
}
