package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2024/7/6 3:43
 **/
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api("分类接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping
    @ApiOperation("新增分类接口")
    public Result insertCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.insertCategory(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分类分页接口")
    public Result<PageResult> pageCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult page = categoryService.pageCategory(categoryPageQueryDTO);
        return Result.success(page);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用分类")
    public void startOrStop(@PathVariable Integer status, Long id) {
        Category category = new Category();
        category.builder().status(status).id(id).build();
        categoryService.startOrStop(category);
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> selectCategoryByType(Integer type) {
        List<Category> list = categoryService.selectCategoryByType(type);
        return Result.success(list);
    }

    @PutMapping
    @ApiOperation("根据ID修改分类")
    public Result updateCategoryById(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategoryById(categoryDTO);
        return Result.success();
    }

    @GetMapping
    @ApiOperation("根据ID查询分类")
    public Result<Category> selectCategroyById(Integer id) {
        Category category = categoryService.selectCategoryById(id);
        return Result.success(category);
    }

    @DeleteMapping
    @ApiOperation("根据ID删除分类")
    public Result deleteCategoryById(Integer id) {
        categoryService.deleteCategoryById(id);
        return Result.success();
    }
}
