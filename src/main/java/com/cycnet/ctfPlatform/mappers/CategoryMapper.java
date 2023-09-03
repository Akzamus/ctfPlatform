package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Mappable<Category, CategoryRequestDto, CategoryResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Category toEntity(CategoryRequestDto request);

}
