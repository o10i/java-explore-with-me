package ru.practicum.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.dto.NewCategoryDto;
import ru.practicum.common.model.Category;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}
