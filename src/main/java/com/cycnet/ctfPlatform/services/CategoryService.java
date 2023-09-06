package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;

public interface CategoryService {

    PageResponseDto<CategoryResponseDto> getAllCategories(int page, int size);
    CategoryResponseDto getCategoryById(long id);
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto);
    void deleteCategory(long id);

}
