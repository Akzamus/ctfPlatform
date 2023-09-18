package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.PageResponseDto;

public interface CrudService<ENTITY, REQUEST, RESPONSE> {

    PageResponseDto<RESPONSE> getAll(int pageNumber, int pageSize);
    RESPONSE getById(long id);
    RESPONSE create(REQUEST requestDto);
    RESPONSE update(long id, REQUEST requestDto);
    void delete(long id);

    ENTITY getEntityById(long id);

}
