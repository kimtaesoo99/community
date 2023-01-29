package com.example.community.dto.category;

import com.example.community.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private List<CategoryResponseDto> children;

    public static List<CategoryResponseDto> toDtoList(List<Category> categories) {
        CategoryHelper helper = CategoryHelper.newInstance(
            categories,
            c -> new CategoryResponseDto(c.getId(), c.getName(), new ArrayList<>()),
            Category::getParent,
            Category::getId,
            CategoryResponseDto::getChildren);
        return helper.convert();
    }
}
