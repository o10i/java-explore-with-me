package ru.practicum.APIadmin.service;

import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto newCategoryDto, Long catId);
}
