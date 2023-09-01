package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.category.CategoryRequest;
import com.cycnet.ctfPlatform.dto.category.CategoryResponse;
import org.mapstruct.Mapper;
import com.cycnet.ctfPlatform.models.Category;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category categoryRequestToCategory(CategoryRequest request);

    CategoryResponse categoryToCategoryResponse(Category category);

}
