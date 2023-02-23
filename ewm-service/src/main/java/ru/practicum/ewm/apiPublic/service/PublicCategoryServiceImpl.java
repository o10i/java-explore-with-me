package ru.practicum.ewm.apiPublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.common.mapper.CategoryMapper.toCategoryDtoList;

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
