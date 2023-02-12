package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryRequestDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Category save(CategoryRequestDto categoryRequestDto) {
        return repository.save(CategoryMapper.toCategory(categoryRequestDto));
    }

    @Override
    public void delete(Long catId) {
        getByIdWithCheck(catId);
        repository.deleteById(catId);
    }

    @Override
    public Category update(CategoryRequestDto categoryRequestDto, Long catId) {
        Category category = getByIdWithCheck(catId);
        category.setName(categoryRequestDto.getName());
        return repository.save(category);
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {
        return repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList());
    }

    @Override
    public Category getById(Long catId) {
        return getByIdWithCheck(catId);
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
