package ru.practicum.service;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto newCategoryDto, Long catId);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);
}
