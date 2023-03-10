package ru.practicum.ewm.apiAdmin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.dto.NewCategoryDto;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.CategoryMapper;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.repository.CategoryRepository;
import ru.practicum.ewm.common.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    CategoryRepository repository;
    EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        getByIdWithCheck(catId);
        if (eventRepository.findAllByCategoryId(catId).size() == 0) {
            repository.deleteById(catId);
        } else {
            throw new ForbiddenException("The category is not empty");
        }
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, Long catId) {
        Category category = getByIdWithCheck(catId);
        if (newCategoryDto.getName() == null) {
            throw new BadRequestException("Field: name. Error: must not be blank. Value: null");
        }
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
