package ru.practicum.APIadmin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.dto.NewCategoryDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CategoryMapper;
import ru.practicum.common.model.Category;
import ru.practicum.common.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;

    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        getByIdWithCheck(catId);
        repository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, Long catId) {
        Category category = getByIdWithCheck(catId);
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}