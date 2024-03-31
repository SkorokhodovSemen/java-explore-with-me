package ru.practicum.categories;

import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);

    CategoryDto getCategoryById(long catId);

    List<CategoryDto> getCategories(int from, int size);

    void deleteCategoryById(long catId);
}
