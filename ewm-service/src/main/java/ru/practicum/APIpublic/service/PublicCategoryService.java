package ru.practicum.APIpublic.service;

import ru.practicum.common.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);
}
