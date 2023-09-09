package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.CategoryMapper;
import com.cycnet.ctfPlatform.models.Category;
import com.cycnet.ctfPlatform.repositories.CategoryRepository;
import com.cycnet.ctfPlatform.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public PageResponseDto<CategoryResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving categories, page number: {}, page size: {}", pageNumber, pageSize);

        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<CategoryResponseDto> categoryPageResponseDto = categoryMapper.toDto(categoryPage);

        log.info("Finished retrieving categories, page number: {}, page size: {}", pageNumber, pageSize);

        return categoryPageResponseDto;
    }

    @Override
    public CategoryResponseDto getById(long id) {
        log.info("Retrieving category by ID: {}", id);

        CategoryResponseDto categoryResponseDto = categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        log.info("Finished retrieving category by ID: {}", id);

        return categoryResponseDto;
    }

    @Override
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
        log.info("Creating a new category with name: {}", categoryRequestDto.name());

        throwExceptionIfCategoryExists(categoryRequestDto.name());

        Category category = categoryMapper.toEntity(categoryRequestDto);
        category = categoryRepository.save(category);
        CategoryResponseDto categoryResponseDto = categoryMapper.toDto(category);

        log.info("Created a new category with name: {}", category.getName());

        return categoryResponseDto;
    }

    @Override
    @Transactional
    public CategoryResponseDto update(long id, CategoryRequestDto categoryRequestDto) {
        log.info("Updating category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        String newName = categoryRequestDto.name();

        if (!category.getName().equals(newName)) {
            throwExceptionIfCategoryExists(newName);
            category.setName(newName);
            category = categoryRepository.save(category);
        }

        CategoryResponseDto categoryResponseDto = categoryMapper.toDto(category);

        log.info("Updated category with ID: {}", id);

        return categoryResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting category with ID: {}", id);

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        categoryRepository.delete(existingCategory);

        log.info("Deleted category with ID: {}", id);
    }

    private void throwExceptionIfCategoryExists(String categoryName) {
        categoryRepository.findByName(categoryName)
                .ifPresent(foundCategory -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the name " + foundCategory.getName() + " already exists."
                    );
                });
    }

}
