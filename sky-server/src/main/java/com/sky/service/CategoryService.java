package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    void insertCategory(CategoryDTO categoryDTO);

    PageResult pageCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    void startOrStop(Category category);

    List<Category> selectCategoryByType(Integer type);

    void updateCategoryById(CategoryDTO categoryDTO);

    Category selectCategoryById(Integer id);

    void deleteCategoryById(Integer id);
}
