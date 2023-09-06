package com.cycnet.ctfPlatform.dto;

import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public record PageResponseDto<T> (
        int currentPageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLastPage,
        boolean isFirstPage,
        boolean isEmpty,
        List<T> content
) {

    public PageResponseDto(
            int currentPageNumber,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean isLastPage,
            boolean isFirstPage,
            boolean isEmpty,
            List<T> content
    ) {
        this.currentPageNumber = currentPageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLastPage = isLastPage;
        this.isFirstPage = isFirstPage;
        this.isEmpty = isEmpty;
        this.content = content == null ? Collections.emptyList() : content;
    }

}
