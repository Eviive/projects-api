package com.eviive.personalapi.dto.pagination;

import org.springframework.data.domain.Slice;

import java.util.List;

public record SliceDTO<T>(
    List<T> content,
    SliceInfosDTO slice
) {

    public static <T> SliceDTO<T> of(final Slice<T> slice) {
        return new SliceDTO<>(
            slice.getContent(),
            new SliceInfosDTO(
                slice.getNumber(),
                slice.getSize(),
                slice.getNumberOfElements(),
                slice.hasContent(),
                slice.isFirst(),
                slice.isLast(),
                slice.hasNext(),
                slice.hasPrevious()
            )
        );
    }

    record SliceInfosDTO(
        int number,
        int size,
        int numberOfElements,
        boolean hasContent,
        boolean first,
        boolean last,
        boolean next,
        boolean previous
    ) {

    }

}
