package ru.practicum.service;

import ru.practicum.dto.CategoryRequestDto;
import ru.practicum.model.Category;

import java.util.List;

public interface CategoryService {

    Category save(CategoryRequestDto categoryRequestDto);

    void delete(Long catId);

    Category update(CategoryRequestDto categoryRequestDto, Long catId);

    List<Category> getAll(Integer from, Integer size);

    Category getById(Long catId);
}
