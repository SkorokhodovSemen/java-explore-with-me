package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.model.CategoryMapper;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper
                .toCategoriesDto(categoryRepository.save(CategoryMapper.toCategories(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("Category with id = {} not found", catId);
            return new NotFoundException("Category not found");
        });
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoriesDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("Category with id = {} not found", catId);
            return new NotFoundException("Category not found");
        });
        return CategoryMapper.toCategoriesDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoriesDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategoryById(long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("Category with id = {} not found", catId);
            return new NotFoundException("Category not found");
        });
        categoryRepository.deleteById(catId);
    }
}
