package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(long id);
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto);
    void deleteCategory(long id);

}
