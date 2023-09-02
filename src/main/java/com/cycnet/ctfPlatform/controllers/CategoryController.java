package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.category.CategoryRequestDto;
import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            @PathVariable @Positive(message = "Id must be positive") Long id
    ) {
        CategoryResponseDto responseDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto responseDto = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto responseDto = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable @Positive(message = "Id must be positive") Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
