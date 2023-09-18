package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.models.Category;

public interface CategoryService extends CrudService<Category, CategoryRequestDto, CategoryResponseDto> {

}
