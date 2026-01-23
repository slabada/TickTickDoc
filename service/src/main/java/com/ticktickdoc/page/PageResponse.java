package com.ticktickdoc.page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalPages,
        long totalElements
) {}
