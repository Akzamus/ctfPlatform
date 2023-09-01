package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    Category getCategoryById(long id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(long id);

}
