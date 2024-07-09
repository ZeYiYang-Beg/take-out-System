package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品ID查询套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    /**
     * 根据套餐ID查询菜品ID
     * @param id
     * @return
     */

    @Select("select dish_id from setmeal_dish where setmeal_id = #{id}")
    List<Dish> getDishIdsBySetmealId(Long id);

    /**
     * 根据套餐ID集合删除对应关系
     * @param idsn
     */
    void delete(List<Long> idsn);

    /**
     * 根据套餐ID删除菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where id = #{id}")
    void deleteBySetMealId(Long id);

    /**
     * 将菜品批量插入到套餐
     * @param setmealDishes
     */
    @AutoFill(OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);
}
