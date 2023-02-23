package ru.practicum.ewm.apiAdmin.service;

import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto newCategoryDto, Long catId);
}
