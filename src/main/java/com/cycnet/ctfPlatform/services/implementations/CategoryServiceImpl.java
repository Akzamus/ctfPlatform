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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public PageResponseDto<CategoryResponseDto> getAll(int pageNumber, int pageSize) {
        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return categoryMapper.toDto(categoryPage);
    }

    @Override
    public CategoryResponseDto getById(long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));
    }

    @Override
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByName(categoryRequestDto.name())
                .ifPresent(foundCategory -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the name " + foundCategory.getName() + " already exists."
                    );
                });

        Category category = categoryMapper.toEntity(categoryRequestDto);
        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto update(long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        String newName = categoryRequestDto.name();

        if(!category.getName().equals(newName)) {
            categoryRepository.findByName(newName)
                    .ifPresent(foundCategory -> {
                        throw new EntityAlreadyExistsException(
                                "Category with the name " + foundCategory.getName() + " already exists."
                        );
                    });
            category.setName(newName);
            category = categoryRepository.save(category);
        }

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        categoryRepository.delete(existingCategory);
    }

}
