package ru.practicum.APIpublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CategoryMapper;
import ru.practicum.common.model.Category;
import ru.practicum.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;


    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return CategoryMapper.toCategoryDtoList(repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Override
    public CategoryDto getById(Long catId) {
        return CategoryMapper.toCategoryDto(getByIdWithCheck(catId));
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
