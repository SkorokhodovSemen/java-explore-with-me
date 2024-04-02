package ru.practicum.categories.model;

import ru.practicum.categories.dto.CategoryDto;

public abstract class CategoryMapper {
    public static CategoryDto toCategoriesDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category toCategories(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        if (categoryDto.getId() != 0) {
            category.setId(categoryDto.getId());
        }
        return category;
    }
}
