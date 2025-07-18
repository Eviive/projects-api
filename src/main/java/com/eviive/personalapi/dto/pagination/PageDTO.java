package com.eviive.personalapi.dto.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageDTO<T>(
    List<T> content,
    PageInfosDTO page
) {

    public static <T> PageDTO<T> of(final Page<T> page) {
        return new PageDTO<>(
            page.getContent(),
            new PageInfosDTO(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.hasContent(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getTotalPages(),
                page.getTotalElements()
            )
        );
    }

    record PageInfosDTO(
        int number,
        int size,
        int numberOfElements,
        boolean hasContent,
        boolean first,
        boolean last,
        boolean next,
        boolean previous,
        int totalPages,
        long totalElements
    ) {

    }

}
