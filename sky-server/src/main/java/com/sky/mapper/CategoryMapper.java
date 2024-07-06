package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2024/7/6 3:44
 **/
@Mapper
public interface CategoryMapper {
    @Insert("insert into category (name, sort, type) values (#{name}, #{sort}, #{type})")
    @AutoFill(value = OperationType.INSERT)
    void insertCategory(Category category);


    Page<Category> queryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void startOrStop(Category category);

    List<Category> selectCategoryByType(Integer type);

    @Select("select * from category where id = #{id}")
    Category selectCategoryById(Integer id);

    @Delete("delete from category where id = #{id}")
    void deleteCategoryById(Integer id);
}
