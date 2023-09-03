package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.exceptions.category.CategoryAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.category.CategoryNotFoundException;
import com.cycnet.ctfPlatform.mappers.CategoryMapper;
import com.cycnet.ctfPlatform.models.Category;
import com.cycnet.ctfPlatform.repositories.CategoryRepository;
import com.cycnet.ctfPlatform.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDto(categories);
    }

    @Override
    public CategoryResponseDto getCategoryById(long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " does not exist."));
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByName(categoryRequestDto.name())
                .ifPresent(foundCategory -> {
                    throw new CategoryAlreadyExistsException(
                            "Category with the name " + foundCategory.getName() + " already exists."
                    );
                });

        Category category = categoryMapper.toEntity(categoryRequestDto);
        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " does not exist."));

        String categoryName = categoryRequestDto.name();

        categoryRepository.findByName(categoryName)
                .ifPresent(foundCategory -> {
                    throw new CategoryAlreadyExistsException(
                            "Category with the name " + foundCategory.getName() + " already exists."
                    );
                });

        category.setName(categoryName);
        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " does not exist."));

        categoryRepository.delete(existingCategory);
    }

}
