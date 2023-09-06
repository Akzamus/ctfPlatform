package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

public interface Mappable<ENTITY, REQUEST, RESPONSE> {

    RESPONSE toDto(ENTITY entity);

    List<RESPONSE> toDto(List<ENTITY> entities);

    ENTITY toEntity(REQUEST request);

    List<ENTITY> toEntity(List<REQUEST> requests);

    @Mapping(target = "currentPageNumber", source = "number")
    @Mapping(target = "pageSize", source = "size")
    @Mapping(target = "isLastPage", source = "last")
    @Mapping(target = "isFirstPage", source = "first")
    @Mapping(target = "isEmpty", source = "empty")
    PageResponseDto<RESPONSE> toDto(Page<ENTITY> page);

}