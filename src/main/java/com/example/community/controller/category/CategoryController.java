package com.example.community.controller.category;

import com.example.community.dto.category.CategoryCreateRequestDto;
import com.example.community.response.Response;
import com.example.community.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Category Controller", tags = "Category")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "모든 카테고리 조회", notes = "모든 카테고리를 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findAllCategories() {
        return Response.success(categoryService.findAllCategory());
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCategory(@Valid @RequestBody CategoryCreateRequestDto req) {
        categoryService.createCategory(req);
        return Response.success();
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCategory(@ApiParam(value = "카테고리 id", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Response.success();
    }
}
