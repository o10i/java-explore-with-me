package ru.practicum.APIpublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.model.Category;
import ru.practicum.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.common.mapper.CategoryMapper.toCategoryDtoList;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;


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
