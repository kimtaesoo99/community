package com.example.community.service.category;

import com.example.community.domain.category.Category;
import com.example.community.dto.category.CategoryCreateRequestDto;
import com.example.community.dto.category.CategoryResponseDto;
import com.example.community.exception.CategoryNotFoundException;
import com.example.community.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAllCategory() {
        List<Category> categories = categoryRepository.findAllOrderByParent();
        return CategoryResponseDto.toDtoList(categories);
    }

    @Transactional
    public void createCategory(CategoryCreateRequestDto req) {
        Category parent = Optional.ofNullable(req.getParentId())
            .map(id -> categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new))
            .orElse(null);
        categoryRepository.save(new Category(req.getName(), parent));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }
}
