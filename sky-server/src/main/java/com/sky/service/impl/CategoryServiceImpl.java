package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import com.sky.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2024/7/6 3:44
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public void insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.insertCategory(category);
    }

    @Override
    public PageResult pageCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 开始分页
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.queryCategory(categoryPageQueryDTO);
        Long total = page.getTotal();
        List<Category> category = page.getResult();
        return new PageResult(total, category);
    }

    @Override
    public void startOrStop(Category category) {
        categoryMapper.startOrStop(category);
    }

    @Override
    public List<Category> selectCategoryByType(Integer type) {
        List<Category> list = categoryMapper.selectCategoryByType(type);
        return list;
    }



    @Override
    public void updateCategoryById(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.startOrStop(category);
    }

    @Override
    public Category selectCategoryById(Integer id) {
        Category category = categoryMapper.selectCategoryById(id);
        return category;
    }

    @Override
    public void deleteCategoryById(Integer id) {
        categoryMapper.deleteCategoryById(id);
    }
}
