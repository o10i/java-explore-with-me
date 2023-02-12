package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.mapper.CategoryMapper.toCategoryDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        return toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
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
        return toCategoryDto(repository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return toCategoryDtoList(repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Override
    public CategoryDto getById(Long catId) {
        return toCategoryDto(getByIdWithCheck(catId));
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
